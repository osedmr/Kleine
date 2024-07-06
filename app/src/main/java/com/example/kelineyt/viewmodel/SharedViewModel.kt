import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _query = MutableLiveData<String>()
    val query: LiveData<String> get() = _query

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }
}
