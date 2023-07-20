package scalaOOP

object Enums {

    // enum keyword only works in scala 3
    // i am using scala 2.13

    /*enum Permissions {
      case READ, WRITE, EXECUTE, NONE

      def openDocument(): Unit =
        if (this == READ) println("opening document...")
        else println("reading not allowed")
    }

    val somePermissions: Permissions = Permissions.READ

    enum PermissionsWithBits(bits: Int) {
      case READ extends PermissionsWithBits(4)
      case WRITE extends PermissionsWithBits(2)
      case EXECUTE extends PermissionsWithBits(1)
      case NONE extends PermissionsWithBits(0)
    }

    object PermissionsWithBits {
      def fromBits(bits: Int): PermissionsWithBits =
      PermissionsWithBits.NONE
    }

    val somePermissionsOrdinal = somePermissions.ordinal
    val allPermissions = PermissionsWithBits.values
    val readPermission: Permissions = Permissions.valueOf("READ")

    def main(args: Array[String]) Unit = {
      somePermissions.openDocument()
      println(somePermissionsOrdinal)
    }
     */
}
