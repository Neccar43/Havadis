import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.novacodestudios.havadis.R
import com.novacodestudios.havadis.databinding.FooterItemBinding

class FooterAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<FooterAdapter.FooterStateViewHolder>() {

    override fun onBindViewHolder(holder: FooterStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): FooterStateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.footer_item, parent, false)
        val binding = FooterItemBinding.bind(view)
        return FooterStateViewHolder(binding, retry)
    }

    class FooterStateViewHolder(private val binding: FooterItemBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.visibility =
                    if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
                btnRetry.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
                tvError.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
            }

        }
    }
}
