package com.dailynews.dailynews.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dailynews.dailynews.R
import com.dailynews.dailynews.ui.MainActivity
import com.dailynews.dailynews.ui.viewmodel.NewsViewModel
import com.dailynews.dailynews.viewholders.NewsAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved.*

class SavedFragment : Fragment() {

    private val viewModel : NewsViewModel by activityViewModels()
    private lateinit var newsAdapter : NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycler()

        // load saved news
        viewModel.getSavedNews().observe(viewLifecycleOwner, {
            newsAdapter.differ.submitList(it)
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val currItem = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(currItem)
                Snackbar.make(view, "Deleted successfully", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.upsert(currItem)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(savedRecyclerView)
        }

    }

    private fun setUpRecycler() {
        newsAdapter = NewsAdapter()
        savedRecyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_savedFragment_to_selectedFragment, bundle)
        }
    }

}