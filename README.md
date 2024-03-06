# FinDeGrado
Realizado por Pedro Bañeres y Sergio Fuentes, 2ºDAM asignatura Desarrollo de Interfaces, profesor Julio Galaron Tourino.
Es una aplicación que gestiona un banco,  través de ella puedes enviar dinero a otros usuarios además de recibirlo. Por otro lado tiene un chat que permite comunicarse con el soporte de la app por si ocurre algún fallo.
Tiene una BBDD llamada Bank, con 3 tablas users, accounts y transactions.
Por otro lado el proyecto consta de distintos paquetes, un paquete que gestiona las cuentas y sus modificaciones en la BBDD y una clase en específico para el gráfico que muestra diariamente los gastos del usuario.
Lo siguiente serían los distintos controladores, donde está el mediador o MasterController, el LoginController y el RegisterController, además hemos diferenciado los controladores para admin (las funcionalidades se ampliarán en un futuro cercano) y los controladores para el usuario o cliente.
Además de esto tenemos un paquete para las excepciones personalizadas que hemos necesitado para la creación del código.
Y por último una clase usuario con los atributos necesarios y 1 main duplicado para ejecutar la aplicación.
En la carpeta resources tenemos las vistas, las imagenes y los css.

Nos hemos servido de guía para elaborar el diseño de: https://www.figma.com/community/file/1095690747392065253/banking-app-fintech?searchSessionId=lsvi9ivg-hzehy4nsgt4

Es necesario para la creación de la BBDD ejecutar ScriptSQLEjecutar, donde realiza la creación de la BBDD, las tablas e inserta valores.