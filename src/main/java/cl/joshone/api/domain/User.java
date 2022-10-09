package cl.joshone.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tbl_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
	
	@Id
	//@SequenceGenerator(name = "mySeqGen", sequenceName = "myDbSeq", initialValue = 1, allocationSize = 1)
	//@GeneratedValue(strategy = GenerationType.AUTO, generator = "myDbSeq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "apellido")
	private String apellido;
	@Column(name = "email")
	private String email;
	@Column(name = "password")
	private String password;
	@Column(name = "genero")
	private String genero;
	@Column(name = "rol")
	private String rol;
	
}
