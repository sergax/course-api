package com.sergax.courseapi.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity {
    @Column(name = "name")
    String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    Set<User> users;
}
