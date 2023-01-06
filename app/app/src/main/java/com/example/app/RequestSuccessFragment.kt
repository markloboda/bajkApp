package com.example.app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

class RequestSuccessFragment : Fragment(R.layout.request_success_fragment) {

    private lateinit var requestActivity: RequestActivity

    private lateinit var gifImageView: GifImageView
    private lateinit var gifDrawable: GifDrawable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestActivity = activity as RequestActivity
        gifDrawable = GifDrawable(resources, R.drawable.check)
        gifImageView = view.findViewById(R.id.gifImageView)

        gifDrawable.addAnimationListener { loopNumber ->
            gifDrawable.stop()
            requestActivity.finish()
        }
        gifImageView.setImageDrawable(gifDrawable)
    }

}