package com.altunoymak.esarj.presentation.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.altunoymak.esarj.R
import com.altunoymak.esarj.databinding.FragmentSearchBinding
import com.altunoymak.esarj.presentation.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by activityViewModels()
    private lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        observeData()

        binding.searchToolbar.title = getString(R.string.search_charge_location_text)
        binding.searchToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.postDelayed({
                    v.showKeyboard()
                }, 1000)
            }
        }
        binding.searchView.requestFocusFromTouch()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.search(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText ?: "")
                return true
            }
        })
    }

    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewState.collect { viewState ->
                viewState.suggestions?.let { suggestions ->
                    if (suggestions.isEmpty()) {
                        binding.noResultsTextView.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    } else {
                        adapter.submitList(suggestions)
                        binding.noResultsTextView.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                }
                viewState.errorMessage?.let {
                    binding.noResultsTextView.visibility = View.VISIBLE
                    binding.noResultsTextView.text = it
                }
            }
        }
    }

    private fun initAdapter() {
        adapter = SearchAdapter {suggestion ->
            viewModel.selectSuggestion(suggestion)
            val action = SearchFragmentDirections.actionSearchFragmentToMapsFragment()
            findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}