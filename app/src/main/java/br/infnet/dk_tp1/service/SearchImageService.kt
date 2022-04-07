package br.infnet.dk_tp1.service


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class SearchedImageURL(
    val big:String
)
interface SearchImageServiceListener {
    fun whenGetImageFinished(tvshow: SearchedImageURL?)
    fun whenHttpError(erro:String)
}
interface SearchImageApi{
    @GET("/pesquisaImagem")
    fun getImage(@Query("palavraChave") tvshowTitle:String):Call<SearchedImageURL?>?
}
class SearchImageService{
    private lateinit var api: SearchImageApi
    private lateinit var listenerImgService:SearchImageServiceListener
    init{
        val retr = Retrofit.Builder()
            .baseUrl("https://notecompletion.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create()).build()

        api = retr.create(SearchImageApi::class.java)

    }
    fun setListener(listener:SearchImageServiceListener){
        listenerImgService = listener
    }
    fun getImage(tvshowTitle:String) {
        val call = api.getImage(tvshowTitle)
        call!!.enqueue(object : Callback<SearchedImageURL?> {


            override fun onResponse(
                call: Call<SearchedImageURL?>,
                response: Response<SearchedImageURL?>
            ) {
                if (response.isSuccessful) {
                    listenerImgService.whenGetImageFinished(response.body())
                }
            }

            override fun onFailure(call: Call<SearchedImageURL?>, t: Throwable) {
                listenerImgService.whenHttpError("retrofit parou mano: ${t.message}")
            }

        })
    }

}
