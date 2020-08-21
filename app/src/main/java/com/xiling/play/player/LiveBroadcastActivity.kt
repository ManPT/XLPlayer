package com.xiling.play.player

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.ImageFormat
import android.hardware.Camera
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.*
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.WindowManager
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import com.alex.livertmppushsdk.FdkAacEncode
import com.alex.livertmppushsdk.RtmpSessionManager
import com.alex.livertmppushsdk.SWVideoEncoder
import com.lib.base.BaseActivity
import com.lib.tools.LogTool
import com.lib.tools.ScreenTool
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xiling.play.R
import kotlinx.android.synthetic.main.activity_livebroadcast.*
import java.io.DataOutputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

const val RTMP_URL : String = "_rtmpUrl"


/**
 * 主播直播页面
 */
class LiveBroadcastActivity : BaseActivity() {
    var rxPermissions: RxPermissions? = null
    private var _rtmpUrl :String = "rtmp://push01.axiling.com/ty/2020061201"

    override fun getContentViewId(): Int {
       return R.layout.activity_livebroadcast
    }


    override fun getIntentData(intent: Intent?) {
       if (intent!!.getStringExtra(RTMP_URL) != null){
           _rtmpUrl = intent!!.getStringExtra(RTMP_URL)
       }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rxPermissions = RxPermissions(this);
    }
    @SuppressLint("InvalidWakeLockTag", "CheckResult")
    override fun requestData() {
        rxPermissions!!
            .request(
                Manifest.permission.CAMERA
                , Manifest.permission.RECORD_AUDIO
                , Manifest.permission.WAKE_LOCK
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.CHANGE_NETWORK_STATE
                , Manifest.permission.CHANGE_WIFI_STATE
                , Manifest.permission.ACCESS_NETWORK_STATE
                , Manifest.permission.ACCESS_WIFI_STATE
            )
            .subscribe { granted ->
                if (granted) { // Always true pre-M
                    InitAll()
                    InitAudioRecord()
                    RtmpStartMessage()
                    val pm =
                        getSystemService(Context.POWER_SERVICE) as PowerManager
                    _wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag")
                } else {
                    Toast.makeText(this, "部分权限未打开，请打开后重试", Toast.LENGTH_LONG)
                }
            }
    }

    private val ID_RTMP_PUSH_START: Int = 100
    private val ID_RTMP_PUSH_EXIT = 101
    private var WIDTH_DEF = 480
    private var HEIGHT_DEF = 640
    private val FRAMERATE_DEF = 20
    private val BITRATE_DEF = 800 * 1000

    private val SAMPLE_RATE_DEF = 22050
    private val CHANNEL_NUMBER_DEF = 2
    private val DEBUG_ENABLE = false
    var _wakeLock: PowerManager.WakeLock? = null
    private var _outputStream: DataOutputStream? = null

    private var _AudioRecorder: AudioRecord? = null
    private var _RecorderBuffer: ByteArray? = null
    private var _fdkaacEnc: FdkAacEncode? = null
    private var _fdkaacHandle = 0

    private var _mCamera: Camera? = null
    private var _bIsFront = true
    private var _swEncH264: SWVideoEncoder? = null
    private var _iDegrees = 0

    private var _iRecorderBufferSize = 0

    private var _SwitchCameraBtn: Button? = null

    private var _bStartFlag = false

    private var _iCameraCodecType = ImageFormat.NV21

    private var _yuvNV21 = ByteArray(WIDTH_DEF * HEIGHT_DEF * 3 / 2)
    private var _yuvEdit = ByteArray(WIDTH_DEF * HEIGHT_DEF * 3 / 2)

    private var _rtmpSessionMgr: RtmpSessionManager? = null

    private var _YUVQueue: Queue<ByteArray> =
        LinkedList()
    private var _yuvQueueLock: Lock = ReentrantLock()
    private var _h264EncoderThread: Thread? = null

    private var _h264Runnable = Runnable {
        while (!Thread.interrupted() && _bStartFlag) {
            val iSize = _YUVQueue.size
            if (iSize > 0) {
                _yuvQueueLock.lock()
                val yuvData = _YUVQueue.poll()
                if (iSize > 9) {
                    LogTool.d(TAG!!,"###YUV Queue len=" + _YUVQueue.size + ", YUV length=" + yuvData!!.size)
                }
                _yuvQueueLock.unlock()
                if (yuvData == null) {
                    continue
                }
                _yuvEdit = if (_bIsFront) {
                    _swEncH264!!.YUV420pRotate270(yuvData, HEIGHT_DEF, WIDTH_DEF)
                } else {
                    _swEncH264!!.YUV420pRotate90(yuvData, HEIGHT_DEF, WIDTH_DEF)
                }
                val h264Data = _swEncH264!!.EncoderH264(_yuvEdit)
                if (h264Data != null) {
                    _rtmpSessionMgr!!.InsertVideoData(h264Data)
                    if (DEBUG_ENABLE) {
                        try {
                            _outputStream!!.write(h264Data)
                            val iH264Len = h264Data.size
                            //Log.i(LOG_TAG, "Encode H264 len="+iH264Len);
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        }
                    }
                }
            }
            try {
                Thread.sleep(1)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        _YUVQueue.clear()
    }

    private var _aacEncoderRunnable = Runnable {
        var outputStream: DataOutputStream? = null
        if (DEBUG_ENABLE) {
            val saveDir = Environment.getExternalStorageDirectory()
            val strFilename = "$saveDir/aaa.aac"
            try {
                outputStream = DataOutputStream(FileOutputStream(strFilename))
            } catch (e1: FileNotFoundException) {
                e1.printStackTrace()
            }
        }
        var lSleepTime = SAMPLE_RATE_DEF * 16 * 2 / _RecorderBuffer!!.size.toLong()
        while (!Thread.interrupted() && _bStartFlag) {
            var iPCMLen =
                _AudioRecorder!!.read(_RecorderBuffer, 0, _RecorderBuffer!!.size) // Fill buffer
            if (iPCMLen != AudioRecord.ERROR_BAD_VALUE && iPCMLen != 0) {
                if (_fdkaacHandle != 0) {
                    val aacBuffer =
                        _fdkaacEnc!!.FdkAacEncode(_fdkaacHandle, _RecorderBuffer)
                    if (aacBuffer != null) {
                        val lLen = aacBuffer.size.toLong()
                        _rtmpSessionMgr!!.InsertAudioData(aacBuffer)
                        //Log.i(LOG_TAG, "fdk aac length="+lLen+" from pcm="+iPCMLen);
                        if (DEBUG_ENABLE) {
                            try {
                                outputStream!!.write(aacBuffer)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            } else {
                LogTool.d(TAG!!,"######fail to get PCM data")
            }
            try {
                Thread.sleep(lSleepTime / 10)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        LogTool.d(TAG!!, "AAC Encoder Thread ended ......")
    }
    private var _AacEncoderThread: Thread? = null

    private fun getDispalyRotation(): Int {
        val i: Int? = getWindowManager()?.getDefaultDisplay()?.getRotation()
        when (i) {
            Surface.ROTATION_0 -> return 0
            Surface.ROTATION_90 -> return 90
            Surface.ROTATION_180 -> return 180
            Surface.ROTATION_270 -> return 270
        }
        return 0
    }

    private fun getDisplayOritation(degrees: Int, cameraId: Int): Int {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)
        var result = 0
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360
            result = (360 - result) % 360
        } else {
            result = (info.orientation - degrees + 360) % 360
        }
        return result
    }

    private fun InitAll() {
        val wm: WindowManager = getWindowManager()

        val width = wm.defaultDisplay.width
        val height = wm.defaultDisplay.height
        val iNewWidth = (height * 3.0 / 4.0).toInt()

        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        val iPos = width - iNewWidth
        layoutParams.setMargins(iPos, 0, 0, 0)

        surfaceViewEx!!.holder.setFixedSize(HEIGHT_DEF, WIDTH_DEF)
        surfaceViewEx!!.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        surfaceViewEx!!.holder.setKeepScreenOn(true)
        surfaceViewEx!!.holder.addCallback(SurceCallBack())
        surfaceViewEx!!.layoutParams = layoutParams
    }

    private val _previewCallback =
        Camera.PreviewCallback { YUV, currentCamera ->
            if (!_bStartFlag) {
                return@PreviewCallback
            }
            val bBackCameraFlag = true
            var yuv420: ByteArray? = null
            if (_iCameraCodecType == ImageFormat.YV12) {
                yuv420 = ByteArray(YUV.size)
                _swEncH264!!.swapYV12toI420_Ex(YUV, yuv420, HEIGHT_DEF, WIDTH_DEF)
            } else if (_iCameraCodecType == ImageFormat.NV21) {
                yuv420 = _swEncH264!!.swapNV21toI420(YUV, HEIGHT_DEF, WIDTH_DEF)
            }
            if (yuv420 == null) {
                return@PreviewCallback
            }
            if (!_bStartFlag) {
                return@PreviewCallback
            }
            _yuvQueueLock.lock()
            if (_YUVQueue.size > 1) {
                _YUVQueue.clear()
            }
            _YUVQueue.offer(yuv420)
            _yuvQueueLock.unlock()
        }




    fun InitCamera() {
        val p = _mCamera!!.parameters
        val prevewSize = p.previewSize
        HEIGHT_DEF = prevewSize.width
        WIDTH_DEF = prevewSize.height
        Log.d("pangtao","WIDTH_DEF = " +WIDTH_DEF)
        Log.d("pangtao","HEIGHT_DEF = " +HEIGHT_DEF)
        var params :RelativeLayout.LayoutParams  = RelativeLayout.LayoutParams(-1,-1)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        params.width = ScreenTool.getWindowWidth(this)
        params.height = ScreenTool.getWindowWidth(this)*HEIGHT_DEF/WIDTH_DEF
        surfaceViewEx.layoutParams = params

        //surfaceViewEx.resize(ScreenTool.getWindowWidth(this),ScreenTool.getWindowHeight(this))

        LogTool.d(
            TAG!!,
            "Original Width:" + prevewSize.width + ", height:" + prevewSize.height
        )
        val PreviewSizeList = p.supportedPreviewSizes
        val pictureSizes = p.getSupportedPictureSizes()


        val PreviewFormats = p.supportedPreviewFormats
        var iNV21Flag = 0
        var iYV12Flag = 0
        for (yuvFormat in PreviewFormats) {
            LogTool.d(TAG!!, "preview formats:$yuvFormat")
            if (yuvFormat == ImageFormat.YV12) {
                iYV12Flag = ImageFormat.YV12
            }
            if (yuvFormat == ImageFormat.NV21) {
                iNV21Flag = ImageFormat.NV21
            }
        }
        if (iNV21Flag != 0) {
            _iCameraCodecType = iNV21Flag
        } else if (iYV12Flag != 0) {
            _iCameraCodecType = iYV12Flag
        }
        p.setPreviewSize(HEIGHT_DEF, WIDTH_DEF)
        p.previewFormat = _iCameraCodecType
        p.previewFrameRate = FRAMERATE_DEF
        _mCamera!!.setDisplayOrientation(_iDegrees)
        p.setRotation(_iDegrees)
        _mCamera!!.setPreviewCallback(_previewCallback)
        _mCamera!!.parameters = p
        try {
            _mCamera!!.setPreviewDisplay(surfaceViewEx!!.holder)
        } catch (e: Exception) {
            return
        }
        _mCamera!!.cancelAutoFocus() //只有加上了这一句，才会自动对焦。
        _mCamera!!.startPreview()
    }

    private inner class SurceCallBack : SurfaceHolder.Callback {
        override fun surfaceChanged(
            holder: SurfaceHolder,
            format: Int,
            width: Int,
            height: Int
        ) {
            _mCamera!!.autoFocus(Camera.AutoFocusCallback { success, camera ->
                if (success) {
                    InitCamera()
                    camera.cancelAutoFocus() //只有加上了这一句，才会自动对焦。
                }
            })
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            _iDegrees = getDisplayOritation(getDispalyRotation(), 0)
            if (_mCamera != null) {
                InitCamera()
                return
            }
            _mCamera =
                Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
            InitCamera()
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {}
    }


    private fun Start() {
        if (DEBUG_ENABLE) {
            val saveDir = Environment.getExternalStorageDirectory()
            val strFilename = "$saveDir/aaa.h264"
            try {
                _outputStream = DataOutputStream(FileOutputStream(strFilename))
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        _rtmpSessionMgr = RtmpSessionManager()
        _rtmpSessionMgr!!.Start(_rtmpUrl)
        val iFormat = _iCameraCodecType
        _swEncH264 = SWVideoEncoder(WIDTH_DEF, HEIGHT_DEF, FRAMERATE_DEF, BITRATE_DEF)
        _swEncH264!!.start(iFormat)
        _bStartFlag = true
        _h264EncoderThread = Thread(_h264Runnable)
        _h264EncoderThread!!.priority = Thread.MAX_PRIORITY
        _h264EncoderThread!!.start()
        _AudioRecorder!!.startRecording()
        _AacEncoderThread = Thread(_aacEncoderRunnable)
        _AacEncoderThread!!.priority = Thread.MAX_PRIORITY
        _AacEncoderThread!!.start()
    }

    private fun stop() {
        _bStartFlag = false
        _AacEncoderThread!!.interrupt()
        _h264EncoderThread!!.interrupt()
        _AudioRecorder!!.stop()
        _swEncH264!!.stop()
        _rtmpSessionMgr!!.Stop()
        _yuvQueueLock.lock()
        _YUVQueue.clear()
        _yuvQueueLock.unlock()
        if (DEBUG_ENABLE) {
            if (_outputStream != null) {
                try {
                    _outputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 切换摄像头
     */
    private fun switchCameraEvent() {
        if (_mCamera == null) {
            return
        }
        _mCamera!!.setPreviewCallback(null)
        _mCamera!!.stopPreview()
        _mCamera!!.release()
        _mCamera = null
        _mCamera = if (_bIsFront) {
            Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
        } else {
            Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
        }
        _bIsFront = !_bIsFront
        InitCamera()
    }


    private fun InitAudioRecord() {
        _iRecorderBufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE_DEF,
            AudioFormat.CHANNEL_CONFIGURATION_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        _AudioRecorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE_DEF, AudioFormat.CHANNEL_CONFIGURATION_STEREO,
            AudioFormat.ENCODING_PCM_16BIT, _iRecorderBufferSize
        )
        _RecorderBuffer = ByteArray(_iRecorderBufferSize)
        _fdkaacEnc = FdkAacEncode()
        _fdkaacHandle = _fdkaacEnc!!.FdkAacInit(SAMPLE_RATE_DEF, CHANNEL_NUMBER_DEF)
    }

    private var mHandler: PlayHandler = PlayHandler()

    private inner class PlayHandler : Handler {
        constructor()

        override fun handleMessage(msg: Message) {
            val b = msg.data
            var ret: Int
            when (msg.what) {
                ID_RTMP_PUSH_START -> {
                    Start()
                }
            }
        }
    }

    private fun RtmpStartMessage() {
        val msg = Message()
        msg.what = ID_RTMP_PUSH_START
        val b = Bundle()
        b.putInt("ret", 0)
        msg.data = b
        mHandler.sendMessage(msg)
    }


    override fun onResume() {
        super.onResume()
        _wakeLock!!.acquire()
    }


    override fun onPause() {
        super.onPause()
        _wakeLock!!.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
        stop()
    }
}
