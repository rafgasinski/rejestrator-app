package com.example.rejestrator.view.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.rejestrator.R
import com.example.rejestrator.view.State
import com.example.rejestrator.view.model.entities.EmployeeLoginData
import com.example.rejestrator.view.model.repositories.ApiRepository
import com.example.rejestrator.view.viewmodel.LoginEmployeeViewModel
import kotlinx.android.synthetic.main.fragment_login_employee.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginEmployee : Fragment() {

    lateinit var loginEmployeeViewModel: LoginEmployeeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_employee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ChangeToAdminLoginButton.setOnClickListener { x -> x.findNavController().navigate(R.id.action_loginEmployee_to_loginAdmin) }

        inputID.addTextChangedListener{
            infoAboutLogin.visibility = View.VISIBLE
            infoAboutLogin1.visibility = View.GONE
        }

        inputPin.addTextChangedListener{
            infoAboutLogin.visibility = View.VISIBLE
            infoAboutLogin1.visibility = View.GONE
        }

        loginEmployeeViewModel = ViewModelProvider(requireActivity()).get(LoginEmployeeViewModel::class.java)

        LoginButtonEmployee.setOnClickListener{x ->
            var id = inputID.text.toString()
            var pin = inputPin.text.toString()
            var employeeLoginData : EmployeeLoginData

            if(id.isNullOrEmpty() || pin.isNullOrEmpty())
                Toast.makeText(requireContext(), "Nie wpisano danych do logowania.", Toast.LENGTH_SHORT).show()
            else{

                var loginCall = ApiRepository.canEmployeeLogin(id, pin)

                loginCall.enqueue(object : Callback<EmployeeLoginData>{
                    override fun onFailure(call: Call<EmployeeLoginData>, t: Throwable) {
                        Toast.makeText(requireContext(), "Błąd! Nie połączono z bazą danych.", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<EmployeeLoginData>, response: Response<EmployeeLoginData>) {
                        if(response.code() == 200)
                        {
                            employeeLoginData = response.body()!!
                            State.currentEmployeeId = employeeLoginData.employeeID
                            State.currentEmployeeShift = employeeLoginData.shift
                            x.findNavController().navigate(R.id.action_loginEmployee_to_dashboardTaskListEmployee)
                        }
                        else if(response.code() == 404){
                            inputID.setText("")
                            inputPin.setText("")

                            infoAboutLogin1.visibility = View.VISIBLE
                            infoAboutLogin.visibility = View.GONE
                        }
                    }

                })
            }
        }
        //add logic to logic
    }

    companion object {
        @JvmStatic
        fun newInstance(): LoginEmployee {
            return LoginEmployee()
        }
    }
}