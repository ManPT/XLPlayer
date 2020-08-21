package com.xiling.play.player

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.lib.base.BaseActivity
import com.mr.ijkplayer.widget.media.IjkVideoView
import com.xiling.play.R
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 * 直播-用户界面
 */
class IJKPlayerActivity : BaseActivity() {
    val VIDEO_PATH ="play_vdeo_path"
    private var mVideoPath: String? = null
    private var mVideoView: IjkVideoView? = null

    override fun getContentViewId(): Int {
       return R.layout.activity_i_j_k_player
    }

    override fun getIntentData(intent: Intent?) {
        if(!TextUtils.isEmpty(intent?.getStringExtra(VIDEO_PATH))){
            mVideoPath = intent?.getStringExtra(VIDEO_PATH);

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mVideoPath = "rtmp://live01.axiling.com/ty/2020061201"

        IjkMediaPlayer.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin("libijkplayer.so")

        mVideoView =
            findViewById<View>(R.id.video_view) as IjkVideoView
        mVideoView!!.setVideoPath(mVideoPath)
        mVideoView!!.setOnErrorListener(object : IMediaPlayer.OnErrorListener {
            override fun onError(iMediaPlayer: IMediaPlayer?, i: Int, i1: Int): Boolean {
                mVideoView!!.start()
                return true
            }
        })
        mVideoView!!.start()
    }

    override fun requestData() {

    }


    override fun onResume() {
        super.onResume()
        mVideoView!!.resume()
    }

    override fun onStop() {
        super.onStop()
        IjkMediaPlayer.native_profileEnd()
        mVideoView!!.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mVideoView!!.stopPlayback()
    }

}
