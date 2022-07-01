package com.example.nikestore.feature.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.nikestore.R
import com.example.nikestore.common.NikeFragment
import com.example.nikestore.databinding.FragmentProfileBinding
import com.example.nikestore.feature.auth.AuthActivity
import com.example.nikestore.feature.favorites.FavoriteProductsActivity
import org.koin.android.ext.android.inject

class ProfileFragment : NikeFragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by inject()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favoriteProductsBtn.setOnClickListener {
            startActivity(Intent(requireContext(), FavoriteProductsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        checkAuthState()
    }

    private fun checkAuthState() {
        if (viewModel.isSignedIn) {
            binding.authBtn.text = getString(R.string.signOut)
            binding.authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_out, 0)
            binding.usernameTv.text = viewModel.username
            binding.authBtn.setOnClickListener {
                viewModel.signOut()
                checkAuthState()
            }
        } else {
            binding.authBtn.text = getString(R.string.signIn)
            binding.authBtn.setOnClickListener {
                startActivity(Intent(requireContext(), AuthActivity::class.java))
            }
            binding.authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_in, 0)
            binding.usernameTv.text = getString(R.string.guest_user)
        }
    }
}