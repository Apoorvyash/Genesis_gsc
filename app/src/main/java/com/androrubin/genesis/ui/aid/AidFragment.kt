package com.androrubin.genesis.ui.aid

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.androrubin.genesis.databinding.FragmentAidBinding
import com.androrubin.genesis.map_work.MapsActivity
import com.androrubin.genesis.ui.aid.book_appointment.BookAppointmentActivity
import com.androrubin.genesis.ui.aid.book_appointment.ScheduleAppointment
import com.androrubin.genesis.ui.aid.book_caretaker.ScheduleCareTaker

class AidFragment : Fragment() {

    private var _binding: FragmentAidBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel = ViewModelProvider(this).get(AidViewModel::class.java)

        _binding = FragmentAidBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.bookAppointmentCard.setOnClickListener {
            val intent = Intent(context, ScheduleAppointment::class.java)
            startActivity(intent)
        }

        binding.mapBtn.setOnClickListener {

            val intent = Intent(context, MapsActivity::class.java)
            startActivity(intent)

        }
        binding.bookCaretakerCard.setOnClickListener {
            val intent = Intent(context, ScheduleCareTaker::class.java)
            startActivity(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}