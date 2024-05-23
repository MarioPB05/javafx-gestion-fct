
# Aplicación para la gestión de FCT

Este repositorio contiene el proyecto desarrollado para la asignatura de Entornos de Desarrollo. La aplicación tiene como objetivo gestionar las FCT (Formación en Centros de Trabajo) de una institución educativa.

El proyecto consiste en una aplicación que proporciona herramientas para la gestión de empresas, tutores laborales, representantes legales, tutores y alumnos.


## Mejoras de presentación

La primera mejora de presentación que se nota a la vista es el hecho de separar la aplicación en varias ventanas o secciones. De esta manera, cuando entra el usuario únicamente ve un menú atractivo y bastante guiado.

Las opciones de este menú están estratégicamente colocadas, ya que van desde los primeros datos que debes rellenar hasta el último.

Aparte, en cada ventana de gestión, tanto empresas, alumnos, tutores, como asignaciones, se ha optado por un diseño homogéneo para permitirle al usuario acostumbrarse a la ventana una única vez.

Por último, también he optado por añadirle colores e iconos a los botones, así como tooltips, que le indican en todo momento al usuario las acciones que puede realizar sobre la aplicación.

## Mejoras de funcionalidad

En cuanto a funcionalidad se ha agregado en la gran mayoría de las pantallas de formularios una previa validación, para que en todo momento los datos que se van a insertar en base de datos sea homogéneos.

La validación más complicada ha sido el echo del CIF en las empresas, ya que parece un número aleatorio, pero por detrás tiene una lógica muy interesante.

En cuanto a código, se han creado muchas funciones de utilidades, para poder exprimir al máximo las funciones y tener el código lo más limpio, legible y único posible. En parte esto es gracias a utilizar el patrón de diseño MVC, o también conocido como Modelo-Vista-Controlador.

Por último, un agregado tanto de funcionalidad como de diseño ha sido el echo de poder tener registros personalizados en la consola. Que muestran en todo momento el estado de la aplicación, y en caso de error, la aplicación no se inmuta y únicamente muestra un registro por error.

## Script SQL

A continuación se inserta el script sql para generar las tablas necesarias por la aplicación:

```sql
CREATE TABLE company_manager (
    id INT PRIMARY KEY AUTO_INCREMENT,
    dni VARCHAR(9) NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL
);

CREATE TABLE company_tutor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    dni VARCHAR(9) NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    phone VARCHAR(30) NOT NULL
);

CREATE TABLE company (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cif VARCHAR(9) NOT NULL,
    name VARCHAR(50) NOT NULL,
    address VARCHAR(50) NOT NULL,
    postal_code VARCHAR(5) NOT NULL,
    city VARCHAR(50) NOT NULL,
    journey_type VARCHAR(50) NOT NULL,
    modality VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    manager_id INT NOT NULL,
    tutor_id INT NOT NULL,
    FOREIGN KEY (manager_id) REFERENCES company_manager(id),
    FOREIGN KEY (tutor_id) REFERENCES company_tutor(id)
);

CREATE TABLE student (
    id INT PRIMARY KEY AUTO_INCREMENT,
    dni VARCHAR(9) NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    birthdate DATE NOT NULL
);

CREATE TABLE tutors (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    phone VARCHAR(30) NOT NULL
);

CREATE TABLE assignment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    company_id INT NOT NULL,
    tutor_id INT NOT NULL,
    creation_date DATE NOT NULL,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (company_id) REFERENCES company(id),
    FOREIGN KEY (tutor_id) REFERENCES tutors(id)
);
```

## Modelo relacional de la base de datos
![Base de Datos](https://github.com/MarioPB05/javafx-gestion-fct/blob/5507ffbb7b01447ac44accc7a6a419b0297ec181/src/main/resources/images/sql.png?raw=true)