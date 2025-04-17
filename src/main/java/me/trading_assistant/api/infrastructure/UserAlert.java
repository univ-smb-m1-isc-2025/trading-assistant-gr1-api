package me.trading_assistant.api.infrastructure;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_alerts")
@Getter
@Setter
public class UserAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private Account user;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private String alertType; // Type d'alerte (ex: "moving_average_cross", "price_variation", etc.)

    @Column
    private Double threshold; // Seuil pour l'alerte (ex: pourcentage ou valeur)

    @Column
    private Integer days; // Nombre de jours pour les moyennes (si applicable)

    @Column
    private String pattern; // Nom du pattern détecté (si applicable)

    @Column
    private Double priceLevel; // Niveau de prix pour le franchissement de seuil (si applicable)
}