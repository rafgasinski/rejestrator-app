package com.example.rejestrator.view.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.rejestrator.R
import com.example.rejestrator.view.State
import com.example.rejestrator.view.model.entities.AdminLoginData
import com.example.rejestrator.view.model.entities.EmployeeLoginData
import com.example.rejestrator.view.model.repositories.ApiRepository
import com.example.rejestrator.view.viewmodel.LoginAdminViewModel
import com.example.rejestrator.view.viewmodel.LoginEmployeeViewModel
import kotlinx.android.synthetic.main.fragment_login_administrator.*
import kotlinx.android.synthetic.main.fragment_login_employee.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class LoginAdministrator : Fragment() {

    lateinit var loginAdministratorViewModel: LoginAdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_administrator, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoginButtonAdmin.setOnClickListener { x -> x.findNavController().navigate(R.id.action_loginAdmin_to_dashboardLogsListAdmin) }
        ChangeToEmployeeLoginButton.setOnClickListener { x -> x.findNavController().navigate(R.id.action_loginAdmin_to_loginEmployee) }

        inputUsername.addTextChangedListener{
            infoAboutLogin2.visibility = View.GONE
        }

        inputPassword.addTextChangedListener{
            infoAboutLogin2.visibility = View.GONE
        }

        loginAdministratorViewModel = ViewModelProvider(requireActivity()).get(LoginAdminViewModel::class.java)

        LoginButtonAdmin.setOnClickListener { x ->
            var username = inputUsername.text.toString()
            var password = inputPassword.text.toString()
            var adminLoginData: AdminLoginData

            if (username.isNullOrEmpty() || password.isNullOrEmpty())
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_data),
                    Toast.LENGTH_SHORT
                ).show()
            else {

                val cred = "${username}:${password}"

                val auth = "Basic ${Base64.getEncoder().encodeToString(cred.toByteArray())}"

                var loginCall = ApiRepository.canAdminLogin(auth)

                loginCall.enqueue(object : Callback<AdminLoginData> {
                    override fun onFailure(call: Call<AdminLoginData>, t: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.no_conn),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<AdminLoginData>,
                        response: Response<AdminLoginData>
                    ) {
                        if (response.code() == 200) {
                            adminLoginData = response.body()!!
                            State.currentAdminUsername = username
                            State.currentAdminPassword = String(Base64.getDecoder().decode(adminLoginData.password))
                            x.findNavController().navigate(R.id.action_loginAdmin_to_dashboardLogsListAdmin)
                        } else if (response.code() == 404) {
                            inputUsername.setText("")
                            inputPassword.setText("")

                            infoAboutLogin2.visibility = View.VISIBLE
                        }
                    }

                })
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(): LoginAdministrator {
            return LoginAdministrator()
        }
    }
}