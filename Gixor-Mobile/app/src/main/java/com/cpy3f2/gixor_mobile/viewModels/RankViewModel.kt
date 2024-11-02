import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RankViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RankUiState())
    val uiState: StateFlow<RankUiState> = _uiState.asStateFlow()

    init {
        // 监听筛选条件变化并触发搜索
        viewModelScope.launch {
            combine(
                uiState.map { it.selectedDomain },
                uiState.map { it.selectedCountry },
                uiState.map { it.selectedLanguage }
            ) { domain, country, language ->
                Triple(domain, country, language)
            }
            .debounce(300L) // 防抖，避免频繁请求
            .collect { (domain, country, language) ->
                fetchRankings(domain, country, language)
            }
        }
    }

    fun updateDomain(domain: String) {
        _uiState.update { it.copy(selectedDomain = domain) }
    }

    fun updateCountry(country: String) {
        _uiState.update { it.copy(selectedCountry = country) }
    }

    fun updateLanguage(language: String) {
        _uiState.update { it.copy(selectedLanguage = language) }
    }

    private suspend fun fetchRankings(domain: String?, country: String?, language: String?) {
        // 实现获取排行榜数据的逻辑
    }
}

data class RankUiState(
    val selectedDomain: String? = null,
    val selectedCountry: String? = null,
    val selectedLanguage: String? = null,
    val rankings: List<GitHubUser> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 