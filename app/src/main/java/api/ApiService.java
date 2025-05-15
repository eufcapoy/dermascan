package api;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface ApiService {
    // Register endpoint
    // Register endpoint
    @POST("/api/auth/register")
    Call<User> register(@Body User user);

    // Login endpoint
    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @PUT("/api/auth/profile")
    Call<User> updateProfile(
            @Body User user,
            @Header("Authorization") String authToken
    );
    @PUT("/api/auth/change-password")
    Call<Void> changePassword(
            @Body ChangePasswordRequest request,
            @Header("Authorization") String token
    );
    @POST("/api/auth/upload-scan")
    Call<Void> uploadScan(
            @Part MultipartBody.Part image,
            @Header("Authorization") String authToken
    );
}
