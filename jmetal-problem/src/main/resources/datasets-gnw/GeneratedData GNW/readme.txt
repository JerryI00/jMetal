FICHEROS goldstandard:
	- Contienen la información sobre los enlaces entre los distintos genes. Por cada dos genes habrá una línea [A B "] que indica si hay interaccion regulatoria
	  entre A y B poniendo un "=1 si la hay o =0 si no
FICHEROS goldstandard-signed:
	- Contienen informacion similar a los godstandard, pero esta vez la informacion que recogen sobre la relacion entre los dos genes es si es inibitoria "=- o
	  aceleradora "=+
	- Estos ficheros son mas pequeños ya que solo recogen la informacion de los genes que interaccionan entre ellos [goldstandar "=1]
FICHEROS <name>.xml
	- Guardan el modelo dinámico de la vía en lenguaje System Biology Markup Language (SBML)
	-Estos ficheros solo pueden ser abiertos por GNW, no es compatible con otras herramientas de SBML
FICHEROS wildtype
	- Guardan el wild-type en estado estacionario
	- Etos ficheros contienen 1 linea de informacion y una adicional a modo de cabecera
FICHEROS knockouts, knockdowns y extensiones
	- Guardan la información de los knockout y los heterozygous knockdowns respectivamente.
	- Cada linea corresponde con un experimento (exceptuando la primera que es una cabecera) y cada columna corresponde con un gen correspondiente al de su posicion
	  en la cabecera
	- Son ficheros que contienen información en matrices cuadradas (NxN)
	- Estos ficheros pueden ser cargados y visualizados con Matlab
	EXTENSIONES
		FICH multifactorial.tsv
			- Guardan la informacion del estado estacionario multifactorial
			- Cada linea corresponde a un nivel de perturbacion diferente
		FICH dualknockouts.tsv
			- Guardan la información de los dualknocouts
			- Cada linea corresponde a un nivel de perturbacion diferente
		FICH proteins
			- Contienen la informacion de las concentraciones de la proteina con y sin ruido.
	- Si se realiza el experimento con ruido (noise) se añaden ficheros extra con el experimento realizado antes de añadir el ruido:
		· Aparecen con -nonoise si se realizo con ODE model o -noexpnoise si se hizo con SDE model
FICHEROS timeseries
	- Tienen la sintaxis <name>_<type>_timeseries donde <type> es el tipo de experimento que se ha realizado en cada serie
	- Contienen el estado estacionario de cada experimento.
	- Cada fila es un experimento exceptuando la primera que es una cabecera
	- Cada columna corresponde a un gen exceptuando la primera que indica el tiempo