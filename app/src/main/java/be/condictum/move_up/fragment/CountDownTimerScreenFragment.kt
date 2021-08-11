package be.condictum.move_up.fragment

import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.paperdb.Paper

import android.widget.Toast
import cn.iwgang.countdownview.CountdownView
import kotlin.concurrent.timer


class CountDownTimerScreenFragment : Fragment() {

    private val LIMIT_TIME: Long= 10000
    var isStart = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(be.condictum.move_up.R.layout.fragment_count_down_timer_screen, container, false)

        init()
    }


        private fun init(){
            val btn = requireView()!!.findViewById<Button>(be.condictum.move_up.R.id.cronoBtn)
            val countdown_view = requireView()!!.findViewById<CountdownView>(be.condictum.move_up.R.id.cronometer)
        Paper.init(this.context)
        isStart = Paper.book().read(IS_START_KEY,false)
        if (isStart){
           btn.isEnabled = false
            checktime()
        }
            else{
                btn.isEnabled=true
            }
            btn.setOnClickListener(){

                if (!isStart){
                    countdown_view.start(LIMIT_TIME)
                    Paper.book().write(IS_START_KEY,true)

                }

            }

            countdown_view.setOnCountdownEndListener {
                Toast.makeText(this.context,"finished",Toast.LENGTH_LONG).show()
            }
            countdown_view.setOnCountdownIntervalListener(1000,object:CountdownView.OnCountdownIntervalListener{
                override fun onInterval(cv: CountdownView?, remainTime: Long) {
                    Log.d("Tımer",""+remainTime)
                }

            })
    }
    override fun onStop(){
        val countdown_view = requireView()!!.findViewById<CountdownView>(be.condictum.move_up.R.id.cronometer)
        Paper.book().write(LAST_TIME_SAVED_KEY,System.currentTimeMillis())
        Paper.book().write(TIME_REMAIN,countdown_view.remainTime)
        super.onStop()
    }

    private fun checktime() {
        val countdown_view = requireView()!!.findViewById<CountdownView>(be.condictum.move_up.R.id.cronometer)
        val currentTime = System.currentTimeMillis()
        val lastTimeSaved : Long = Paper.book().read(LAST_TIME_SAVED_KEY,0).toLong()
        val timeRemain : Long = Paper.book().read(TIME_REMAIN,0).toLong()
        val result = timeRemain + (lastTimeSaved-currentTime)
        if (result>0){
            countdown_view.start(result)
        }
        else{
            countdown_view.stop()
            reset()
        }
    }

    private fun reset() {
        val btn = requireView()!!.findViewById<Button>(be.condictum.move_up.R.id.cronoBtn)
        btn.isEnabled=true
        Paper.book().delete(IS_START_KEY)
        Paper.book().delete(TIME_REMAIN)
        Paper.book().delete(LAST_TIME_SAVED_KEY)
        isStart=false
    }

    companion object {
        private const val IS_START_KEY = "IS_START"
        private const val LAST_TIME_SAVED_KEY = "LAST_TIME_SAVED"
        private const val TIME_REMAIN = "TIME_REMAIN"
    }
}