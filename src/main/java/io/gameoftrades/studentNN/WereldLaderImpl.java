package io.gameoftrades.studentNN;

import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.kaart.*;
import io.gameoftrades.model.lader.WereldLader;
import io.gameoftrades.model.markt.Handel;
import io.gameoftrades.model.markt.HandelType;
import io.gameoftrades.model.markt.Handelswaar;
import io.gameoftrades.model.markt.Markt;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WereldLaderImpl implements WereldLader {

    Kaart kaart;
    Stad stad;
    Handel handel;
    Handel handel2;
    Markt markt;
    HandelType handelTypeBied;
    HandelType handelTypeVraag;
    Handelswaar handelswaar;
    Coordinaat coordinaatTerrein;
    Coordinaat coordinaatStad;
    TerreinType terreinType;
    int breedte, hoogte;

    //Values
    String stadsNaam;
    Scanner scanner;

    List<Stad> steden = new ArrayList<Stad>();
    List<Handel> handels = new ArrayList<Handel>();

    @Override
    public Wereld laad(String resource) {
        kaartMaker();
        terreinTypeMaker();
        stedenMaker();
        marktMaker();

        return Wereld.van(kaart, steden, markt);
    }

    public void kaartMaker() {
        String resourceKaart = "/kaarten/westeros-kaart.txt";
        InputStream input;
        input = this.getClass().getResourceAsStream(resourceKaart);
        scanner = new Scanner(input);
        String[] omvangKaart = scanner.nextLine().split(",");
        breedte = Integer.parseInt(omvangKaart[0]);
        hoogte = Integer.parseInt(omvangKaart[1]);
        kaart = Kaart.metOmvang(breedte, hoogte);
    }

    public void terreinTypeMaker() {
        for (int y = 0; y < hoogte; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < line.length(); x++) {
                coordinaatTerrein = Coordinaat.op(x, y);
                char current = line.charAt(x);
                terreinType = TerreinType.fromLetter(current);
                Terrein.op(kaart, coordinaatTerrein, terreinType);
            }
        }
    }

    public void stedenMaker() {
        int aantalsteden = scanner.nextInt();
        scanner.nextLine();
        for (int y = 0; y < aantalsteden; y++) {
            String[] cordinat = scanner.nextLine().split(",");
            int coX = Integer.parseInt(cordinat[0]);
            int coY = Integer.parseInt(cordinat[1]);
            stadsNaam = cordinat[2];
            coordinaatStad = Coordinaat.op(coX, coY);
            stad = Stad.op(coordinaatStad, stadsNaam);
            steden.add(stad);
        }
    }

    public void marktMaker() {
        int aantalMarkten = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < aantalMarkten; i++) {
            String[] markten = scanner.nextLine().split(",");
            markten[0] = stadsNaam;
            String biedtOfVraagt = markten[1];
            String handelswaarNaam = markten[2];
            int prijs = Integer.parseInt(markten[3]);

            handelswaar = new Handelswaar(handelswaarNaam);
            handelTypeBied = HandelType.valueOf(biedtOfVraagt);
            handelTypeVraag = HandelType.valueOf(biedtOfVraagt);

            handel = new Handel(stad, handelTypeBied, handelswaar, prijs);
            handel2 = new Handel(stad, handelTypeVraag, handelswaar, prijs);

            if (handel.equals(handelTypeBied)) {
                handels.add(handel);
            } else if (handel.equals(handelTypeVraag)) {
                handels.add(handel2);
            }
        }
        markt = new Markt(handels);
    }
}