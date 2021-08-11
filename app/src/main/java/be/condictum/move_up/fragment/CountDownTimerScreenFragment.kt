package be.condictum.move_up.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.paperdb.Paper

import android.widget.Toast
import be.condictum.move_up.R
import be.condictum.move_up.databinding.FragmentCountDownTimerScreenBinding
import cn.iwgang.countdownview.CountdownView
import com.google.android.material.textfield.TextInputEditText
import java.sql.Date
import kotlin.concurrent.timer


class CountDownTimerScreenFragment : Fragment() {

    companion object {
        private const val IS_START_KEY = "IS_START"
        private const val LAST_TIME_SAVED_KEY = "LAST_TIME_SAVED"
        private const val TIME_REMAIN = "TIME_REMAIN"
    }

    private var _binding: FragmentCountDownTimerScreenBinding? = null
    private val binding get() = _binding!!

    private var LIMIT_TIME: Long = 10000
    var isStart = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountDownTimerScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTimer()
    }

    private fun initTimer() {
        val btn = binding.cronoBtn
        val countdownView = binding.cronometer

        Paper.init(this.context)
        isStart = Paper.book().read(IS_START_KEY, false)

        if (isStart) {
            btn.isEnabled = false
            checktime()
        } else {
            btn.isEnabled = true
        }

        btn.setOnClickListener {

            val mDialogView =
                LayoutInflater.from(this.context).inflate(R.layout.cronometer_input, null)
            val mBuilder =
                AlertDialog.Builder(this.context).setView(mDialogView).setTitle("Add Goals")
                    .setPositiveButton("Kaydet") { dialogInterface, i ->

                        val hour = mDialogView.findViewById<EditText>(R.id.hour).text.toString()
                        val minute = mDialogView.findViewById<EditText>(R.id.minute).text.toString()
                        val second = mDialogView.findViewById<EditText>(R.id.second).text.toString()

                        LIMIT_TIME = ((hour.toLong()*6000000)+(minute.toLong()*60000)+(second.toLong()*1000))

                        if (!isStart) {
                            countdownView.start(LIMIT_TIME)
                            Paper.book().write(IS_START_KEY, true)
                        }

                    }.setNegativeButton("ÇIK") { _, _ -> }

            mBuilder.show()


        }

        countdownView.setOnCountdownEndListener {
            Toast.makeText(this.context, "finished", Toast.LENGTH_LONG).show()
        }

        countdownView.setOnCountdownIntervalListener(1000) { cv, remainTime ->
            Log.d("Tımer", "" + remainTime)
        }
    }

    override fun onStop() {
        super.onStop()

        val countdownView = binding.cronometer

        Paper.book().write(LAST_TIME_SAVED_KEY, System.currentTimeMillis())
        Paper.book().write(TIME_REMAIN, countdownView.remainTime)
    }

    private fun checktime() {
        val countdownView = binding.cronometer
        val currentTime = System.currentTimeMillis()

        val lastTimeSaved: Long = Paper.book().read(LAST_TIME_SAVED_KEY, 0).toLong()
        val timeRemain: Long = Paper.book().read(TIME_REMAIN, 0).toLong()
        val result = timeRemain + (lastTimeSaved - currentTime)

        if (result > 0) {
            countdownView.start(result)
        } else {
            countdownView.stop()
            reset()
        }
    }

    private fun reset() {
        val btn = binding.cronoBtn
        btn.isEnabled = true
        isStart = false

        Paper.book().delete(IS_START_KEY)
        Paper.book().delete(TIME_REMAIN)
        Paper.book().delete(LAST_TIME_SAVED_KEY)
    }
}