package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;


    public class PrimaryController implements Initializable {

    @FXML Pagination pagination;
    @FXML FlowPane imageFlowPane; // FlowPane para exibir as imagens
    private List<String> waifuImageUrls = new ArrayList<>(); // Lista de URLs das imagens de waifus deliciosas aiai 

    public void carregar() {
        try {
            var url = new URL("https://api.waifu.pics/nsfw/waifu");
            var con = url.openConnection();
            con.connect();
            var is = con.getInputStream();

            var reader = new BufferedReader(new InputStreamReader(is));
            var json = reader.readLine();

            var waifuUrl = jsonParaUrl(json);

            if (waifuUrl != null) {
                waifuImageUrls.add(waifuUrl);
                mostrarImagens();
            } else {
                mostrarMensagem("Não foi possível obter uma imagem de waifu.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensagem("Erro. " + e.getMessage());
        }
    }

    private String jsonParaUrl(String json) throws JsonMappingException, JsonProcessingException {
        var mapper = new ObjectMapper();
        var node = mapper.readTree(json);
        return node.get("url").asText();
    }

    private void mostrarMensagem(String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(mensagem);
        alert.show();
    }

    private void mostrarImagens() {
        
        imageFlowPane.getChildren().clear();

        // Adicione as imagens de waifus ao FlowPane
        waifuImageUrls.forEach(url -> {
            var image = new ImageView(new Image(url));
            image.setFitHeight(150);
            image.setFitWidth(150);
            imageFlowPane.getChildren().add(image);
        });
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        pagination.setPageFactory(pag -> null); // Desabilita a paginação
    }

    @FXML
    private void adicionarImagem() {
        carregar();
    }
}
