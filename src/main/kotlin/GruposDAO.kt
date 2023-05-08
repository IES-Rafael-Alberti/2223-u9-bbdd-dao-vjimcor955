import java.sql.DriverManager
import java.sql.PreparedStatement

class GruposDAO {
    private val connection = DriverManager.getConnection("jdbc:h2:mem:default", "admin", "admin")

    fun infoGrupos (grupo: Int): Any {
        val statementGrupos = connection.prepareStatement("SELECT GRUPOID FROM GRUPOS")
        val statementUnGrupo = connection.prepareStatement("SELECT * FROM GRUPOS WHERE GRUPOID = ?")
        val statementTodosGrupos = connection.prepareStatement("SELECT * FROM GRUPOS")

        val grupos = statementGrupos.toString()

        return if (grupo.toString() in grupos) {
            statementUnGrupo.setInt(1, grupo)
        } else {
            statementTodosGrupos.toString()
        }
    }
}