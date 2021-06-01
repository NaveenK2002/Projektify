package naveen.kadali.projektify.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import naveen.kadali.projektify.activities.SignInActivity
import naveen.kadali.projektify.models.User
import naveen.kadali.projektify.activities.SignUpActivity
import naveen.kadali.projektify.utils.Constants

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun signInUser(activity: SignInActivity){
        mFireStore.collection(Constants.USERS).document(getCurrentUserId()).get().addOnSuccessListener {document->
            val loggedInUser = document.toObject(User::class.java)!!
            if(loggedInUser!=null)
                activity.signInSuccess(loggedInUser)
        }.addOnFailureListener { e->
            Log.e("SignInUser", "Error", e)
        }
    }

    fun registerUser(activity: SignUpActivity, userInfo : User){
            mFireStore.collection(Constants.USERS).document(getCurrentUserId()).set(userInfo, SetOptions.merge()).addOnSuccessListener {
                activity.userRegisteredSuceess()
            }
    }

    fun getCurrentUserId(): String {

        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
}