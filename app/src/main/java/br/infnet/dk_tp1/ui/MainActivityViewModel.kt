package br.infnet.dk_tp1.ui

import br.infnet.dk_tp1.domain.MyFirestoreUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivityViewModel : ViewModel() {
    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val mUserLiveData = MutableLiveData<FirebaseUser?>().apply { value = null }

    fun getLiveDataWhenOtherLiveDataChange(
        changedLiveData: MutableLiveData<FirebaseUser?>,
        functionWhichReturnsNewValue: (FirebaseUser?) -> Boolean
    ): LiveData<Boolean> {
        return Transformations.map(changedLiveData, functionWhichReturnsNewValue)
    }

    val isUserLoggedIn: LiveData<Boolean> =
        getLiveDataWhenOtherLiveDataChange(mUserLiveData, { user ->
            user != null
        })
    val firestoreUser = MutableLiveData<MyFirestoreUser>()

    init {

        mAuth.let {
            mUserLiveData.postValue(it.currentUser)
        }
    }

    fun logout() {
        mAuth?.signOut()
        mUserLiveData.postValue(null)
    }

    private fun createUserByAuth() {
        val mUser: FirebaseUser? = mUserLiveData.value
        mUser?.let {

            val users_collection = Firebase.firestore.collection("users")

            val newuser: MyFirestoreUser? = mUser.email?.let {
                MyFirestoreUser(mUser.uid, mUser.displayName, it)
            }
            if (newuser != null) {
                val task = users_collection.document(mUser.uid).set(newuser)
                task?.addOnSuccessListener {

                }
            }

        }

    }

    fun verifyCurrentUser() {
        mUserLiveData?.value?.let {
            val db = Firebase.firestore
            val users_collection = db.collection("users")
            val userUid = it.uid
            val test = users_collection.document(userUid).get()

            test.addOnFailureListener {

                val dd = 4

            }
            test.addOnSuccessListener { document ->
                val createdUser = document?.toObject(MyFirestoreUser::class.java)
                if (createdUser == null) {
                    createUserByAuth()
                } else {
                    firestoreUser.postValue(createdUser!!)
                }
            }

        }

    }

}