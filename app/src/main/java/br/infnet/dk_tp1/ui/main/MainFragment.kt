package br.infnet.dk_tp1.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.view.children
import androidx.core.view.marginStart
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import br.infnet.dk_tp1.R
import br.infnet.dk_tp1.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }
    private var _binding: MainFragmentBinding? = null
    private val binding get()= _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = MainFragmentBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val viw = binding.root
        return viw

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with (binding.seekBar as SeekBar){
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    binding.viewpgr.currentItem = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    //TODO("Not yet implemented")
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    //TODO("Not yet implemented")
                }

            })
        }

        with(binding.viewpgr as ViewPager2){

            viewModel?.tarefas?.value?.size?.let{
                adapter=SliderAdapter(childFragmentManager,lifecycle,it)
            }
            setupItemTouchPraCima(this)
            this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.seekBar.progress = position
                }
            })
        }

    }

    private fun setupItemTouchPraCima(viewPager2: ViewPager2) {
        viewPager2.children.find { it is RecyclerView }?.let {
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    //todo deletar
                }
            }).attachToRecyclerView(it as RecyclerView)

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}