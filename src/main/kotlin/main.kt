data class Ctf(val id: Int, val grupoId: Int, val puntuacion: Int)
data class Grupo(val grupoid: Int, val mejorCtfId: Int = 0)

fun main(args: Array<String>) {

    val participaciones = mutableListOf<Ctf>(Ctf(1, 1, 3),
                                Ctf(1, 2, 101),
                                Ctf(2, 2, 3),
                                Ctf(2, 1, 50),
                                Ctf(2, 3, 1),
                                Ctf(3, 1, 50),
                                Ctf(3, 3, 5))
    val mejoresCtfByGroupId = calculaMejoresResultados(participaciones)
    println(mejoresCtfByGroupId)

    val grupos = GruposDAO()
    val ctf = CtfDAO()

    println("Introduca el comando:")
    val comando = readln()
    val args: List<String> = comando.split(" ")

    when {
        args[0] == "-a" && args.count() == 4 -> {
            val ctfid = args[1].toInt()
            val grupoId = args[2].toInt()
            val puntuacion = args[3].toInt()

            ctf.addGrupo(Ctf(ctfid, grupoId, puntuacion))
            participaciones.add(Ctf(ctfid, grupoId, puntuacion))

            println("Procesado: Añadida participación del grupo $grupoId en el CTF $ctfid con una puntuación de $puntuacion puntos.")
        }
        args[0] == "-d" && args.count() == 3 -> {
            val ctfid = args[1].toInt()
            val grupoId = args[2].toInt()
            val puntuacion = 0

            ctf.deleteGrupo(Ctf(ctfid, grupoId, puntuacion))
            participaciones.remove(Ctf(ctfid, grupoId, puntuacion))

            println("Procesado: Eliminada participación del grupo $grupoId en el CTF $ctfid.")
        }
        args[0] == "-l" && args.count() == 2 -> {
            val grupoId = args[1].toInt()

            val resultado = grupos.infoGrupos(grupoId)


//            println("Procesado: Listado participación del grupo $grupoId")
//            resultado.forEach {
//                println("GRUPO: $id $desc MEJORCTF: $mejorctf")
//            }
        }
        else -> println("Comando incorrecto, las opciones y su formato son:\n" +
                "'-a <ctfid> <grupoId> <puntuacion>' -> Añade una participación del grupo <grupoid> en el CTF <ctfid> con la puntuación <puntuacion>. Recalcula el campo mejorposCTFid de los grupos en la tabla GRUPOS.\n" +
                "'-d <ctfid> <grupoId>' -> Elimina la participación del grupo <grupoid> en el CTF <ctfid>. Recalcula el campo mejorposCTFid de los grupos en la tabla GRUPOS.\n" +
                "'-l <grupoId>' -> Si <grupoId> esta presente muestra la información del grupo <grupoId>, sino muestra la información de todos los grupos.")
    }

}

/**
 * TODO
 *
 * @param participaciones
 * @return devuelve un mutableMapOf<Int, Pair<Int, Ctf>> donde
 *      Key: el grupoId del grupo
 *      Pair:
 *          first: Mejor posición
 *          second: Objeto CTF el que mejor ha quedado
 */
private fun calculaMejoresResultados(participaciones: List<Ctf>): MutableMap<Int, Pair<Int, Ctf>> {
    val participacionesByCTFId = participaciones.groupBy { it.id }
    var participacionesByGrupoId = participaciones.groupBy { it.grupoId }
    val mejoresCtfByGroupId = mutableMapOf<Int, Pair<Int, Ctf>>()
    participacionesByCTFId.values.forEach { ctfs ->
        val ctfsOrderByPuntuacion = ctfs.sortedBy { it.puntuacion }.reversed()
        participacionesByGrupoId.keys.forEach { grupoId ->
            val posicionNueva = ctfsOrderByPuntuacion.indexOfFirst { it.grupoId == grupoId }
            if (posicionNueva >= 0) {
                val posicionMejor = mejoresCtfByGroupId.getOrDefault(grupoId, null)
                if (posicionMejor != null) {
                    if (posicionNueva < posicionMejor.first)
                        mejoresCtfByGroupId.set(grupoId, Pair(posicionNueva, ctfsOrderByPuntuacion.get(posicionNueva)))
                } else
                    mejoresCtfByGroupId.set(grupoId, Pair(posicionNueva, ctfsOrderByPuntuacion.get(posicionNueva)))

            }
        }
    }
    return mejoresCtfByGroupId
}