package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Table(name = "demo_user"/* , schema = "private_schema" */)
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotBlank(message = "please give a name")
	private String name;
	//@Email(message = "Email is not valid", regexp = "")
	private String email;
	@Min(message = "minimun age required above 18", value = 18)
	private Integer age;
	private String address;
}
