package Common;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by angsuman on 5/19/2016.
 */
public class ExecuteCommandClass {
    @SerializedName("method")
    String Method;
    public void setmethodname(String _Method) {this.Method=_Method;}

    @SerializedName("SQL")
    List<String> SQL;
    public void setSQL(List<String> _SQL){this.SQL=_SQL;}

    @SerializedName("SecutiryCode")
    String SecutiryCode;
    public void setSecutiryCodeL(String _SecutiryCode){this.SecutiryCode=_SecutiryCode;}
}
