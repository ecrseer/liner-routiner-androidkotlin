package br.infnet.dk_tp1.service

import android.os.AsyncTask

class MeuAsyncTasker constructor(val funcao:  ()-> Any)  : AsyncTask<Void,Void,Any>() {

    override fun onPostExecute(result: Any?) {

    }


    override fun doInBackground(vararg params: Void?): Any {
        funcao()
        return 0
    }
}