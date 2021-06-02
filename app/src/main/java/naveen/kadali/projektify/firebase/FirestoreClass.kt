package naveen.kadali.projektify.firebase

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import naveen.kadali.projektify.activities.MainActivity
import naveen.kadali.projektify.activities.MyProfileActivity
import naveen.kadali.projektify.activities.SignInActivity
import naveen.kadali.projektify.models.User
import naveen.kadali.projektify.activities.SignUpActivity
import naveen.kadali.projektify.utils.Constants

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun loadUserData(activity: Activity){
        mFireStore.collection(Constants.USERS).document(getCurrentUserId()).get().addOnSuccessListener {
                document->
            val loggedInUser = document.toObject(User::class.java)!!

            when(activity){
                is SignInActivity-> {
                    activity.signInSuccess(loggedInUser)
                }
                is MainActivity->{
                    activity.updateNavigationUserDetails(loggedInUser)
                }
                is MyProfileActivity->{
                    activity.setUserDataUI(loggedInUser)
                }

            }

        }.addOnFailureListener { e->
            when(activity){
                is SignInActivity-> {
                    activity.hideProgressDialog()
                }
                is MainActivity->{
                    activity.hideProgressDialog()
                }

            }
            Log.e("SignInUser", "Error", e)
        }
    }

    fun registerUser(activity: SignUpActivity, userInfo : User){
            mFireStore.collection(Constants.USERS).document(getCurrentUserId()).set(userInfo, SetOptions.merge()).addOnSuccessListener {
                activity.userRegisteredSuceess()
            }
    }

    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS).document(getCurrentUserId()).update(userHashMap).addOnSuccessListener {
            Log.i(activity.javaClass.simpleName, "Profile data updated successfully")
            Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_LONG).show()
            activity.profileUpdateSuccess()
        }.addOnFailureListener{
            e->
            activity.hideProgressDialog()
            Log.e(activity.javaClass.simpleName, "Profile data update failed!",e)
            Toast.makeText(activity, "Profile update failed!", Toast.LENGTH_LONG).show()
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