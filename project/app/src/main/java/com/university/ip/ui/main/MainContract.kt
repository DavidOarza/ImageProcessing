import android.content.Context
import com.university.ip.model.Photo
import com.university.ip.ui.base.BaseContract

interface MainContract {

    interface View : BaseContract.View {
        //view functions for each change of activity
    }

    interface Presenter {
        //functions that are going to use our library
        fun getAllPicturesFromFolder(context: Context): List<Photo>
    }
}