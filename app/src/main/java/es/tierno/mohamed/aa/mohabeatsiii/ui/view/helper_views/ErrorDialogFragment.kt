package es.tierno.mohamed.aa.mohabeatsiii.ui.view.helper_views

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import es.tierno.mohamed.aa.mohabeatsiii.R

class ErrorDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_ERROR_MESSAGE = "error_message"

        fun newInstance(errorMessage: String): ErrorDialogFragment {
            val fragment = ErrorDialogFragment()
            val args = Bundle()
            args.putString(ARG_ERROR_MESSAGE, errorMessage)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animationView: LottieAnimationView = view.findViewById(R.id.animation_view_error)
        val titleTextView: TextView = view.findViewById(R.id.text_view_dialog_title)
        val messageTextView: TextView = view.findViewById(R.id.text_view_dialog_message)
        val okButton: Button = view.findViewById(R.id.button_dialog_ok)

        val errorMessage = arguments?.getString(ARG_ERROR_MESSAGE) ?: "Ha ocurrido un error desconocido."
        messageTextView.text = errorMessage




        okButton.setOnClickListener {
            dismiss()
        }
    }
}