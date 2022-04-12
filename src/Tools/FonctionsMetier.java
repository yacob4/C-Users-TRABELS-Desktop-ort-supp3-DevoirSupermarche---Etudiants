/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import Entity.Employe;
import Entity.Rayon;
import Entity.Secteur;
import Entity.Travailler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jbuffeteau
 */
public class FonctionsMetier
{
    private PreparedStatement ps;
    private ResultSet rs;
    private Connection cnx;

    public FonctionsMetier()
    {
        cnx = ConnexionBDD.getCnx();
        
    }
    
    public ArrayList<Secteur> GetAllSecteurs()
    {
        ArrayList<Secteur> lesSecteur = new ArrayList<>();
        try {
            ps = cnx.prepareStatement("select numS, nomS from secteur");
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                Secteur sect = new Secteur(rs.getInt("numS"), rs.getString("nomS"));
                lesSecteur.add(sect);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lesSecteur;
    }
    
    public ArrayList<Employe> GetAllEmployes()
    {
        ArrayList<Employe> lesEmploye = new ArrayList<>();
        try {
            ps = cnx.prepareStatement("select numE, prenomE from employe");
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                Employe empl = new Employe(rs.getInt("numE"), rs.getString("prenomE"));
                lesEmploye.add(empl);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lesEmploye;
    }
    
    public ArrayList<Rayon> GetAllRayonsByIdsecteur(int numSecteur)
    {
        ArrayList<Rayon> lesRayon = new ArrayList<>();
        try {
            ps = cnx.prepareStatement("select numR, nomR from rayon WHERE numSecteur="+numSecteur);
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                Rayon ray = new Rayon(rs.getInt("numR"), rs.getString("nomR"));
                lesRayon.add(ray);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lesRayon;
    }
    
    public ArrayList<Travailler> GetAllTravailler(int numRayon)
    {
        ArrayList<Travailler> lesTravaux = new ArrayList<>();
        try {
            ps = cnx.prepareStatement("select employe.numE,employe.prenomE,date,temps from travailler,employe WHERE codeR="+numRayon+" AND travailler.codeE=employe.numE ORDER BY employe.numE");
            rs = ps.executeQuery();
            
            while (rs.next())
            {
                Travailler tra = new Travailler(rs.getInt("numE"), rs.getString("prenomE"),rs.getString("date"), rs.getInt("temps"));
                lesTravaux.add(tra);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lesTravaux;
    }
    
    public int GetIdEmployeByNom(String nomEmploye)
    {
        return 0;
    }
    
    public int TotalHeuresRayon(int numRayon)
    {
        int sommeTmp = 0;
        try {
            ps = cnx.prepareStatement("SELECT SUM(temps) as somme FROM travailler WHERE codeR="+numRayon);
            rs = ps.executeQuery();
            rs.next();
            sommeTmp = rs.getInt("somme");
           
        } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sommeTmp;
    }
    
    public void ModifierTemps(int codeEmploye, int CodeRayon, String uneDate,int nouveauTemps)
    {
        
        try {
            Connection cnx = ConnexionBDD.getCnx();
            PreparedStatement ps = cnx.prepareStatement("UPDATE travailler SET temps ="+nouveauTemps+" WHERE travailler.codeE ="+codeEmploye+" AND travailler.codeR ="+CodeRayon+" AND travailler.date = '"+uneDate+"'");
            ps.executeUpdate();
            
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public void InsererTemps(int codeEmploye, int CodeRayon,int nouveauTemps)
    {
        try {
            Connection cnx = ConnexionBDD.getCnx();
            PreparedStatement ps = cnx.prepareStatement("INSERT INTO travailler VALUES ('"+codeEmploye+"', '"+CodeRayon+"', '2022-04-12', '"+nouveauTemps+"')");
            ps.executeUpdate();
            
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
