package com.example.socialmediaapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class AddPostFrag extends Fragment {

    View v;
    Uri image;
    ImageView imgview;
    EditText caption;
    Button btnaddpost;
    ActivityResultLauncher<Intent> launcher;
    final String IMGBB_API_KEY = "4838077f3adbab432f18d5e7e42a53ff"; // replace with your own

    private void init() {
        imgview = v.findViewById(R.id.dialogueimage);
        caption = v.findViewById(R.id.dialoguecaption);
        btnaddpost = v.findViewById(R.id.btnaddpost);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_post, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getData() != null && result.getResultCode() == RESULT_OK) {
                image = result.getData().getData();
                imgview.setImageURI(image);
            }
        });

        imgview.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            launcher.launch(i);
        });

        btnaddpost.setOnClickListener(v -> {
            if (image == null || caption.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            uploadToImgBB(image, caption.getText().toString().trim());
        });
    }

    private void uploadToImgBB(Uri img, String captiontext) {
        try {
            InputStream is = requireContext().getContentResolver().openInputStream(img);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) bos.write(buf, 0, len);
            String encoded = Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP);

            new Thread(() -> {
                try {
                    String boundary = "*****" + System.currentTimeMillis() + "*****";
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";

                    URL url = new URL("https://api.imgbb.com/1/upload?key=" + IMGBB_API_KEY);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                    DataOutputStream request = new DataOutputStream(conn.getOutputStream());

                    request.writeBytes(twoHyphens + boundary + lineEnd);
                    request.writeBytes("Content-Disposition: form-data; name=\"image\"" + lineEnd);
                    request.writeBytes(lineEnd);
                    request.writeBytes(encoded + lineEnd);
                    request.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    request.flush();
                    request.close();

                    int responseCode = conn.getResponseCode();
                    InputStream responseStream = (responseCode == 200)
                            ? conn.getInputStream()
                            : conn.getErrorStream();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] responseBuf = new byte[1024];
                    int respLen;
                    while ((respLen = responseStream.read(responseBuf)) != -1)
                        baos.write(responseBuf, 0, respLen);

                    String response = baos.toString("UTF-8");

                    if (responseCode == 200) {
                        JSONObject jobj = new JSONObject(response);
                        String link = jobj.getJSONObject("data").getString("url");
                        savePost(link, captiontext);
                    } else {
                        Log.e("ImgBBError", "HTTP " + responseCode + ": " + response);  // <-- Log the full response
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "ImgBB error: " + response, Toast.LENGTH_LONG).show());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Upload failed (network)", Toast.LENGTH_SHORT).show());
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error reading image", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePost(String link, String captiontext) {
        requireActivity().runOnUiThread(() -> {
            String userId = FirebaseAuth.getInstance().getUid();
            if (userId == null) {
                Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
                return;
            }

            HashMap<String, Object> map = new HashMap<>();
            map.put("userID", userId);
            map.put("caption", captiontext);
            map.put("timespam", System.currentTimeMillis() / 1000 + "ago");

            HashMap<String, Object> imageMap = new HashMap<>();
            imageMap.put("0", link);
            map.put("imageUrls", imageMap);

            String key = FirebaseDatabase.getInstance().getReference("posts").push().getKey();
            FirebaseDatabase.getInstance().getReference("posts").child(key)
                    .setValue(map)
                    .addOnSuccessListener(unused -> Toast.makeText(getContext(), "Post uploaded", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to upload post", Toast.LENGTH_SHORT).show());
        });
    }
}
