package com.example.fabio.githubjavapop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.fabio.githubjavapop.Controllers.GitHubApi;
import com.example.fabio.githubjavapop.Controllers.GithubService;
import com.example.fabio.githubjavapop.Controllers.MainAdapter;
import com.example.fabio.githubjavapop.Models.GithubCatalog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.main_recyclerview);

        GithubService service = GitHubApi.getApiClient().create(GithubService.class);
        Call<GithubCatalog> requestCatalog = service.listRepositories("search/repositories?q=language:Java&sort=stars&page=1");

        requestCatalog.enqueue(new Callback<GithubCatalog>() {
            @Override
            public void onResponse(Call<GithubCatalog> call, Response<GithubCatalog> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                }
                else {
                    GithubCatalog repositories = response.body();

                    recyclerView.setAdapter(new MainAdapter(MainActivity.this, repositories.items));
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                            MainActivity.this,
                            LinearLayoutManager.VERTICAL,
                            false);
                    recyclerView.setLayoutManager(layoutManager);
                }
            }

            @Override
            public void onFailure(Call<GithubCatalog> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
