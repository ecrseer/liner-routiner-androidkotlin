package br.infnet.dk_tp1.ui.main

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.infnet.dk_tp1.ui.tarefa.TarefaFragment


class SliderAdapter(
    fa: FragmentManager, lifecycl: Lifecycle,
    var tamanho: Int?
) : FragmentStateAdapter(fa,lifecycl) {
    override
    fun getItemCount(): Int = tamanho ?: 2

    override
    fun createFragment(position: Int): Fragment {
        return tarefaPelaPosicao(position);
    }

    //override
    fun getPageWidth(position: Int): Float {
        return 0.9f
    }

    fun mudaTamanho(novo: Int) {
        if (tamanho != novo) {
            tamanho = novo;
            notifyDataSetChanged()
        }
    }

    private fun tarefaPelaPosicao(position: Int): Fragment {
        val frag = TarefaFragment()
        frag.arguments = bundleOf("posicaoTarefaSelecionada" to position)
        return frag;
    }


    //override
    fun getCount(): Int = getItemCount()

    //override
    fun getItem(position: Int): Fragment {
        return tarefaPelaPosicao(position);
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


}