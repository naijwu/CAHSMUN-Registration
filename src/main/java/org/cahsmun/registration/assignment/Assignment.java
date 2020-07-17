package org.cahsmun.registration.assignment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.cahsmun.registration.delegate.Delegate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name="assignment", schema="public")
public class Assignment implements Serializable {

    @Id
    @Column(name="assignment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long assignment_id;

    @Column(name="country")
    private String country;

    @Column(name="committee")
    private String committee;

    @Column(name="delegate")
    private String delegate;

    /*
    TODO: figure out after displaying country matrix

    @Column(name="delegate_id", insertable=false, updatable=false)
    private long delegate_id;

    @OneToOne(mappedBy="assignment", fetch=FetchType.LAZY, cascade=CascadeType.ALL, optional=false)
    @JsonIgnore
    private Delegate delegate;
    */
}
