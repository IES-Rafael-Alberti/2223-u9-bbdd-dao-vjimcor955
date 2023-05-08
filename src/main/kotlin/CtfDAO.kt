import java.sql.DriverManager

class CtfDAO {
    private val connection = DriverManager.getConnection("jdbc:h2:mem:default", "admin", "admin")

    fun addGrupo (grupo: Ctf): Unit? {
        val statement = connection.prepareStatement("INSERT INTO CTFS (CTFID, GRUPOID, PUNTUACION) VALUES (?, ?, ?)")

        val id = grupo.id
        val grupoId = grupo.grupoId
        val puntuacion = grupo.puntuacion

        statement.setInt(1, id)
        statement.setInt(2, grupoId)
        statement.setInt(3, puntuacion)
        statement.executeUpdate()
        return null
    }

    fun deleteGrupo (grupo: Ctf): Unit? {
        val statement = connection.prepareStatement("DELETE FROM CTFS WHERE GRUPOID = ?")

        val grupoId = grupo.grupoId

        statement.setInt(1, grupoId)
        statement.executeUpdate()
        return null
    }
}