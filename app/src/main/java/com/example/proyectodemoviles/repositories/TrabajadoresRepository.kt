
import com.example.proyectodemoviles.models.Trabajadores
import com.example.proyectodemoviles.models.TrabajadoresList
import com.example.proyectodemoviles.repositories.RetrofitRepository

object TrabajadoresRepository {
    suspend fun getTrabjadoresByCategoria(categoryId: Int, token: String): TrabajadoresList {
        return RetrofitRepository
            .getJsonPlaceholderApi()
            .getWorkersByCategory(categoryId, "Bearer $token")
    }

    suspend fun getTrabajadorById(id:Int, token: String) : Trabajadores{
        return RetrofitRepository
            .getJsonPlaceholderApi()
            .getTrabajadorId(id,"Bearer $token")
    }
}
