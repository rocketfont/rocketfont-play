package undefined.slick
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(AuthGroup.schema, AuthGroupPermissions.schema, AuthPermission.schema, AuthUser.schema, AuthUserGroups.schema, AuthUserUserPermissions.schema, DjangoAdminLog.schema, DjangoContentType.schema, DjangoMigrations.schema, DjangoSession.schema, Font.schema, FontAccessLog.schema, FontCopyright.schema, FontGroup.schema, FontGroupFont.schema, FontLicense.schema, FontPrice.schema, FontUnicode.schema, FontUnicodeSetC.schema, FontUnicodeSetCBak.schema, FontUsageMeasureAccessLog.schema, Group.schema, Member.schema, MemberCreditcard.schema, MemberEmailAuth.schema, MemberFindPassword.schema, MemberFontCopyright.schema, MemberGroup.schema, RegisteredHostname.schema, RegisteredHostnamePending.schema, RocketFontTest.schema, RocketFontTestResult.schema, UrlAccessLog.schema, UrlLetterLog.schema, Urls.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table AuthGroup
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(150,true) */
  case class AuthGroupRow(id: Int, name: String)
  /** GetResult implicit for fetching AuthGroupRow objects using plain SQL queries */
  implicit def GetResultAuthGroupRow(implicit e0: GR[Int], e1: GR[String]): GR[AuthGroupRow] = GR{
    prs => import prs._
    AuthGroupRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table auth_group. Objects of this class serve as prototypes for rows in queries. */
  class AuthGroup(_tableTag: Tag) extends profile.api.Table[AuthGroupRow](_tableTag, Some("rocket_font_main_db"), "auth_group") {
    def * = (id, name) <> (AuthGroupRow.tupled, AuthGroupRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(name))).shaped.<>({r=>import r._; _1.map(_=> AuthGroupRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(150,true) */
    val name: Rep[String] = column[String]("name", O.Length(150,varying=true))

    /** Uniqueness Index over (name) (database name name) */
    val index1 = index("name", name, unique=true)
  }
  /** Collection-like TableQuery object for table AuthGroup */
  lazy val AuthGroup = new TableQuery(tag => new AuthGroup(tag))

  /** Entity class storing rows of table AuthGroupPermissions
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param groupId Database column group_id SqlType(INT)
   *  @param permissionId Database column permission_id SqlType(INT) */
  case class AuthGroupPermissionsRow(id: Int, groupId: Int, permissionId: Int)
  /** GetResult implicit for fetching AuthGroupPermissionsRow objects using plain SQL queries */
  implicit def GetResultAuthGroupPermissionsRow(implicit e0: GR[Int]): GR[AuthGroupPermissionsRow] = GR{
    prs => import prs._
    AuthGroupPermissionsRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table auth_group_permissions. Objects of this class serve as prototypes for rows in queries. */
  class AuthGroupPermissions(_tableTag: Tag) extends profile.api.Table[AuthGroupPermissionsRow](_tableTag, Some("rocket_font_main_db"), "auth_group_permissions") {
    def * = (id, groupId, permissionId) <> (AuthGroupPermissionsRow.tupled, AuthGroupPermissionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(groupId), Rep.Some(permissionId))).shaped.<>({r=>import r._; _1.map(_=> AuthGroupPermissionsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column group_id SqlType(INT) */
    val groupId: Rep[Int] = column[Int]("group_id")
    /** Database column permission_id SqlType(INT) */
    val permissionId: Rep[Int] = column[Int]("permission_id")

    /** Foreign key referencing AuthGroup (database name auth_group_permissions_group_id_b120cbf9_fk_auth_group_id) */
    lazy val authGroupFk = foreignKey("auth_group_permissions_group_id_b120cbf9_fk_auth_group_id", groupId, AuthGroup)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing AuthPermission (database name auth_group_permissio_permission_id_84c5c92e_fk_auth_perm) */
    lazy val authPermissionFk = foreignKey("auth_group_permissio_permission_id_84c5c92e_fk_auth_perm", permissionId, AuthPermission)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)

    /** Uniqueness Index over (groupId,permissionId) (database name auth_group_permissions_group_id_permission_id_0cd325b0_uniq) */
    val index1 = index("auth_group_permissions_group_id_permission_id_0cd325b0_uniq", (groupId, permissionId), unique=true)
  }
  /** Collection-like TableQuery object for table AuthGroupPermissions */
  lazy val AuthGroupPermissions = new TableQuery(tag => new AuthGroupPermissions(tag))

  /** Entity class storing rows of table AuthPermission
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(255,true)
   *  @param contentTypeId Database column content_type_id SqlType(INT)
   *  @param codename Database column codename SqlType(VARCHAR), Length(100,true) */
  case class AuthPermissionRow(id: Int, name: String, contentTypeId: Int, codename: String)
  /** GetResult implicit for fetching AuthPermissionRow objects using plain SQL queries */
  implicit def GetResultAuthPermissionRow(implicit e0: GR[Int], e1: GR[String]): GR[AuthPermissionRow] = GR{
    prs => import prs._
    AuthPermissionRow.tupled((<<[Int], <<[String], <<[Int], <<[String]))
  }
  /** Table description of table auth_permission. Objects of this class serve as prototypes for rows in queries. */
  class AuthPermission(_tableTag: Tag) extends profile.api.Table[AuthPermissionRow](_tableTag, Some("rocket_font_main_db"), "auth_permission") {
    def * = (id, name, contentTypeId, codename) <> (AuthPermissionRow.tupled, AuthPermissionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(name), Rep.Some(contentTypeId), Rep.Some(codename))).shaped.<>({r=>import r._; _1.map(_=> AuthPermissionRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column content_type_id SqlType(INT) */
    val contentTypeId: Rep[Int] = column[Int]("content_type_id")
    /** Database column codename SqlType(VARCHAR), Length(100,true) */
    val codename: Rep[String] = column[String]("codename", O.Length(100,varying=true))

    /** Foreign key referencing DjangoContentType (database name auth_permission_content_type_id_2f476e4b_fk_django_co) */
    lazy val djangoContentTypeFk = foreignKey("auth_permission_content_type_id_2f476e4b_fk_django_co", contentTypeId, DjangoContentType)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)

    /** Uniqueness Index over (contentTypeId,codename) (database name auth_permission_content_type_id_codename_01ab375a_uniq) */
    val index1 = index("auth_permission_content_type_id_codename_01ab375a_uniq", (contentTypeId, codename), unique=true)
  }
  /** Collection-like TableQuery object for table AuthPermission */
  lazy val AuthPermission = new TableQuery(tag => new AuthPermission(tag))

  /** Entity class storing rows of table AuthUser
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param password Database column password SqlType(VARCHAR), Length(128,true)
   *  @param lastLogin Database column last_login SqlType(DATETIME), Default(None)
   *  @param isSuperuser Database column is_superuser SqlType(BIT)
   *  @param username Database column username SqlType(VARCHAR), Length(150,true)
   *  @param firstName Database column first_name SqlType(VARCHAR), Length(30,true)
   *  @param lastName Database column last_name SqlType(VARCHAR), Length(150,true)
   *  @param email Database column email SqlType(VARCHAR), Length(254,true)
   *  @param isStaff Database column is_staff SqlType(BIT)
   *  @param isActive Database column is_active SqlType(BIT)
   *  @param dateJoined Database column date_joined SqlType(DATETIME) */
  case class AuthUserRow(id: Int, password: String, lastLogin: Option[java.sql.Timestamp] = None, isSuperuser: Boolean, username: String, firstName: String, lastName: String, email: String, isStaff: Boolean, isActive: Boolean, dateJoined: java.sql.Timestamp)
  /** GetResult implicit for fetching AuthUserRow objects using plain SQL queries */
  implicit def GetResultAuthUserRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[java.sql.Timestamp]], e3: GR[Boolean], e4: GR[java.sql.Timestamp]): GR[AuthUserRow] = GR{
    prs => import prs._
    AuthUserRow.tupled((<<[Int], <<[String], <<?[java.sql.Timestamp], <<[Boolean], <<[String], <<[String], <<[String], <<[String], <<[Boolean], <<[Boolean], <<[java.sql.Timestamp]))
  }
  /** Table description of table auth_user. Objects of this class serve as prototypes for rows in queries. */
  class AuthUser(_tableTag: Tag) extends profile.api.Table[AuthUserRow](_tableTag, Some("rocket_font_main_db"), "auth_user") {
    def * = (id, password, lastLogin, isSuperuser, username, firstName, lastName, email, isStaff, isActive, dateJoined) <> (AuthUserRow.tupled, AuthUserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(password), lastLogin, Rep.Some(isSuperuser), Rep.Some(username), Rep.Some(firstName), Rep.Some(lastName), Rep.Some(email), Rep.Some(isStaff), Rep.Some(isActive), Rep.Some(dateJoined))).shaped.<>({r=>import r._; _1.map(_=> AuthUserRow.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column password SqlType(VARCHAR), Length(128,true) */
    val password: Rep[String] = column[String]("password", O.Length(128,varying=true))
    /** Database column last_login SqlType(DATETIME), Default(None) */
    val lastLogin: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("last_login", O.Default(None))
    /** Database column is_superuser SqlType(BIT) */
    val isSuperuser: Rep[Boolean] = column[Boolean]("is_superuser")
    /** Database column username SqlType(VARCHAR), Length(150,true) */
    val username: Rep[String] = column[String]("username", O.Length(150,varying=true))
    /** Database column first_name SqlType(VARCHAR), Length(30,true) */
    val firstName: Rep[String] = column[String]("first_name", O.Length(30,varying=true))
    /** Database column last_name SqlType(VARCHAR), Length(150,true) */
    val lastName: Rep[String] = column[String]("last_name", O.Length(150,varying=true))
    /** Database column email SqlType(VARCHAR), Length(254,true) */
    val email: Rep[String] = column[String]("email", O.Length(254,varying=true))
    /** Database column is_staff SqlType(BIT) */
    val isStaff: Rep[Boolean] = column[Boolean]("is_staff")
    /** Database column is_active SqlType(BIT) */
    val isActive: Rep[Boolean] = column[Boolean]("is_active")
    /** Database column date_joined SqlType(DATETIME) */
    val dateJoined: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("date_joined")

    /** Uniqueness Index over (username) (database name username) */
    val index1 = index("username", username, unique=true)
  }
  /** Collection-like TableQuery object for table AuthUser */
  lazy val AuthUser = new TableQuery(tag => new AuthUser(tag))

  /** Entity class storing rows of table AuthUserGroups
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(INT)
   *  @param groupId Database column group_id SqlType(INT) */
  case class AuthUserGroupsRow(id: Int, userId: Int, groupId: Int)
  /** GetResult implicit for fetching AuthUserGroupsRow objects using plain SQL queries */
  implicit def GetResultAuthUserGroupsRow(implicit e0: GR[Int]): GR[AuthUserGroupsRow] = GR{
    prs => import prs._
    AuthUserGroupsRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table auth_user_groups. Objects of this class serve as prototypes for rows in queries. */
  class AuthUserGroups(_tableTag: Tag) extends profile.api.Table[AuthUserGroupsRow](_tableTag, Some("rocket_font_main_db"), "auth_user_groups") {
    def * = (id, userId, groupId) <> (AuthUserGroupsRow.tupled, AuthUserGroupsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(userId), Rep.Some(groupId))).shaped.<>({r=>import r._; _1.map(_=> AuthUserGroupsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(INT) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column group_id SqlType(INT) */
    val groupId: Rep[Int] = column[Int]("group_id")

    /** Foreign key referencing AuthGroup (database name auth_user_groups_group_id_97559544_fk_auth_group_id) */
    lazy val authGroupFk = foreignKey("auth_user_groups_group_id_97559544_fk_auth_group_id", groupId, AuthGroup)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing AuthUser (database name auth_user_groups_user_id_6a12ed8b_fk_auth_user_id) */
    lazy val authUserFk = foreignKey("auth_user_groups_user_id_6a12ed8b_fk_auth_user_id", userId, AuthUser)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)

    /** Uniqueness Index over (userId,groupId) (database name auth_user_groups_user_id_group_id_94350c0c_uniq) */
    val index1 = index("auth_user_groups_user_id_group_id_94350c0c_uniq", (userId, groupId), unique=true)
  }
  /** Collection-like TableQuery object for table AuthUserGroups */
  lazy val AuthUserGroups = new TableQuery(tag => new AuthUserGroups(tag))

  /** Entity class storing rows of table AuthUserUserPermissions
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(INT)
   *  @param permissionId Database column permission_id SqlType(INT) */
  case class AuthUserUserPermissionsRow(id: Int, userId: Int, permissionId: Int)
  /** GetResult implicit for fetching AuthUserUserPermissionsRow objects using plain SQL queries */
  implicit def GetResultAuthUserUserPermissionsRow(implicit e0: GR[Int]): GR[AuthUserUserPermissionsRow] = GR{
    prs => import prs._
    AuthUserUserPermissionsRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table auth_user_user_permissions. Objects of this class serve as prototypes for rows in queries. */
  class AuthUserUserPermissions(_tableTag: Tag) extends profile.api.Table[AuthUserUserPermissionsRow](_tableTag, Some("rocket_font_main_db"), "auth_user_user_permissions") {
    def * = (id, userId, permissionId) <> (AuthUserUserPermissionsRow.tupled, AuthUserUserPermissionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(userId), Rep.Some(permissionId))).shaped.<>({r=>import r._; _1.map(_=> AuthUserUserPermissionsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(INT) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column permission_id SqlType(INT) */
    val permissionId: Rep[Int] = column[Int]("permission_id")

    /** Foreign key referencing AuthPermission (database name auth_user_user_permi_permission_id_1fbb5f2c_fk_auth_perm) */
    lazy val authPermissionFk = foreignKey("auth_user_user_permi_permission_id_1fbb5f2c_fk_auth_perm", permissionId, AuthPermission)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing AuthUser (database name auth_user_user_permissions_user_id_a95ead1b_fk_auth_user_id) */
    lazy val authUserFk = foreignKey("auth_user_user_permissions_user_id_a95ead1b_fk_auth_user_id", userId, AuthUser)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)

    /** Uniqueness Index over (userId,permissionId) (database name auth_user_user_permissions_user_id_permission_id_14a6b632_uniq) */
    val index1 = index("auth_user_user_permissions_user_id_permission_id_14a6b632_uniq", (userId, permissionId), unique=true)
  }
  /** Collection-like TableQuery object for table AuthUserUserPermissions */
  lazy val AuthUserUserPermissions = new TableQuery(tag => new AuthUserUserPermissions(tag))

  /** Entity class storing rows of table DjangoAdminLog
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param actionTime Database column action_time SqlType(DATETIME)
   *  @param objectId Database column object_id SqlType(LONGTEXT), Length(2147483647,true), Default(Some(NULL))
   *  @param objectRepr Database column object_repr SqlType(VARCHAR), Length(200,true)
   *  @param actionFlag Database column action_flag SqlType(SMALLINT UNSIGNED)
   *  @param changeMessage Database column change_message SqlType(LONGTEXT), Length(2147483647,true)
   *  @param contentTypeId Database column content_type_id SqlType(INT), Default(None)
   *  @param userId Database column user_id SqlType(INT) */
  case class DjangoAdminLogRow(id: Int, actionTime: java.sql.Timestamp, objectId: Option[String] = Some("NULL"), objectRepr: String, actionFlag: Int, changeMessage: String, contentTypeId: Option[Int] = None, userId: Int)
  /** GetResult implicit for fetching DjangoAdminLogRow objects using plain SQL queries */
  implicit def GetResultDjangoAdminLogRow(implicit e0: GR[Int], e1: GR[java.sql.Timestamp], e2: GR[Option[String]], e3: GR[String], e4: GR[Option[Int]]): GR[DjangoAdminLogRow] = GR{
    prs => import prs._
    DjangoAdminLogRow.tupled((<<[Int], <<[java.sql.Timestamp], <<?[String], <<[String], <<[Int], <<[String], <<?[Int], <<[Int]))
  }
  /** Table description of table django_admin_log. Objects of this class serve as prototypes for rows in queries. */
  class DjangoAdminLog(_tableTag: Tag) extends profile.api.Table[DjangoAdminLogRow](_tableTag, Some("rocket_font_main_db"), "django_admin_log") {
    def * = (id, actionTime, objectId, objectRepr, actionFlag, changeMessage, contentTypeId, userId) <> (DjangoAdminLogRow.tupled, DjangoAdminLogRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(actionTime), objectId, Rep.Some(objectRepr), Rep.Some(actionFlag), Rep.Some(changeMessage), contentTypeId, Rep.Some(userId))).shaped.<>({r=>import r._; _1.map(_=> DjangoAdminLogRow.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get, _7, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column action_time SqlType(DATETIME) */
    val actionTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("action_time")
    /** Database column object_id SqlType(LONGTEXT), Length(2147483647,true), Default(Some(NULL)) */
    val objectId: Rep[Option[String]] = column[Option[String]]("object_id", O.Length(2147483647,varying=true), O.Default(Some("NULL")))
    /** Database column object_repr SqlType(VARCHAR), Length(200,true) */
    val objectRepr: Rep[String] = column[String]("object_repr", O.Length(200,varying=true))
    /** Database column action_flag SqlType(SMALLINT UNSIGNED) */
    val actionFlag: Rep[Int] = column[Int]("action_flag")
    /** Database column change_message SqlType(LONGTEXT), Length(2147483647,true) */
    val changeMessage: Rep[String] = column[String]("change_message", O.Length(2147483647,varying=true))
    /** Database column content_type_id SqlType(INT), Default(None) */
    val contentTypeId: Rep[Option[Int]] = column[Option[Int]]("content_type_id", O.Default(None))
    /** Database column user_id SqlType(INT) */
    val userId: Rep[Int] = column[Int]("user_id")

    /** Foreign key referencing AuthUser (database name django_admin_log_user_id_c564eba6_fk_auth_user_id) */
    lazy val authUserFk = foreignKey("django_admin_log_user_id_c564eba6_fk_auth_user_id", userId, AuthUser)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing DjangoContentType (database name django_admin_log_content_type_id_c4bce8eb_fk_django_co) */
    lazy val djangoContentTypeFk = foreignKey("django_admin_log_content_type_id_c4bce8eb_fk_django_co", contentTypeId, DjangoContentType)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table DjangoAdminLog */
  lazy val DjangoAdminLog = new TableQuery(tag => new DjangoAdminLog(tag))

  /** Entity class storing rows of table DjangoContentType
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param appLabel Database column app_label SqlType(VARCHAR), Length(100,true)
   *  @param model Database column model SqlType(VARCHAR), Length(100,true) */
  case class DjangoContentTypeRow(id: Int, appLabel: String, model: String)
  /** GetResult implicit for fetching DjangoContentTypeRow objects using plain SQL queries */
  implicit def GetResultDjangoContentTypeRow(implicit e0: GR[Int], e1: GR[String]): GR[DjangoContentTypeRow] = GR{
    prs => import prs._
    DjangoContentTypeRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table django_content_type. Objects of this class serve as prototypes for rows in queries. */
  class DjangoContentType(_tableTag: Tag) extends profile.api.Table[DjangoContentTypeRow](_tableTag, Some("rocket_font_main_db"), "django_content_type") {
    def * = (id, appLabel, model) <> (DjangoContentTypeRow.tupled, DjangoContentTypeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(appLabel), Rep.Some(model))).shaped.<>({r=>import r._; _1.map(_=> DjangoContentTypeRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column app_label SqlType(VARCHAR), Length(100,true) */
    val appLabel: Rep[String] = column[String]("app_label", O.Length(100,varying=true))
    /** Database column model SqlType(VARCHAR), Length(100,true) */
    val model: Rep[String] = column[String]("model", O.Length(100,varying=true))

    /** Uniqueness Index over (appLabel,model) (database name django_content_type_app_label_model_76bd3d3b_uniq) */
    val index1 = index("django_content_type_app_label_model_76bd3d3b_uniq", (appLabel, model), unique=true)
  }
  /** Collection-like TableQuery object for table DjangoContentType */
  lazy val DjangoContentType = new TableQuery(tag => new DjangoContentType(tag))

  /** Entity class storing rows of table DjangoMigrations
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param app Database column app SqlType(VARCHAR), Length(255,true)
   *  @param name Database column name SqlType(VARCHAR), Length(255,true)
   *  @param applied Database column applied SqlType(DATETIME) */
  case class DjangoMigrationsRow(id: Int, app: String, name: String, applied: java.sql.Timestamp)
  /** GetResult implicit for fetching DjangoMigrationsRow objects using plain SQL queries */
  implicit def GetResultDjangoMigrationsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[DjangoMigrationsRow] = GR{
    prs => import prs._
    DjangoMigrationsRow.tupled((<<[Int], <<[String], <<[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table django_migrations. Objects of this class serve as prototypes for rows in queries. */
  class DjangoMigrations(_tableTag: Tag) extends profile.api.Table[DjangoMigrationsRow](_tableTag, Some("rocket_font_main_db"), "django_migrations") {
    def * = (id, app, name, applied) <> (DjangoMigrationsRow.tupled, DjangoMigrationsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(app), Rep.Some(name), Rep.Some(applied))).shaped.<>({r=>import r._; _1.map(_=> DjangoMigrationsRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column app SqlType(VARCHAR), Length(255,true) */
    val app: Rep[String] = column[String]("app", O.Length(255,varying=true))
    /** Database column name SqlType(VARCHAR), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column applied SqlType(DATETIME) */
    val applied: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("applied")
  }
  /** Collection-like TableQuery object for table DjangoMigrations */
  lazy val DjangoMigrations = new TableQuery(tag => new DjangoMigrations(tag))

  /** Entity class storing rows of table DjangoSession
   *  @param sessionKey Database column session_key SqlType(VARCHAR), PrimaryKey, Length(40,true)
   *  @param sessionData Database column session_data SqlType(LONGTEXT), Length(2147483647,true)
   *  @param expireDate Database column expire_date SqlType(DATETIME) */
  case class DjangoSessionRow(sessionKey: String, sessionData: String, expireDate: java.sql.Timestamp)
  /** GetResult implicit for fetching DjangoSessionRow objects using plain SQL queries */
  implicit def GetResultDjangoSessionRow(implicit e0: GR[String], e1: GR[java.sql.Timestamp]): GR[DjangoSessionRow] = GR{
    prs => import prs._
    DjangoSessionRow.tupled((<<[String], <<[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table django_session. Objects of this class serve as prototypes for rows in queries. */
  class DjangoSession(_tableTag: Tag) extends profile.api.Table[DjangoSessionRow](_tableTag, Some("rocket_font_main_db"), "django_session") {
    def * = (sessionKey, sessionData, expireDate) <> (DjangoSessionRow.tupled, DjangoSessionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(sessionKey), Rep.Some(sessionData), Rep.Some(expireDate))).shaped.<>({r=>import r._; _1.map(_=> DjangoSessionRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column session_key SqlType(VARCHAR), PrimaryKey, Length(40,true) */
    val sessionKey: Rep[String] = column[String]("session_key", O.PrimaryKey, O.Length(40,varying=true))
    /** Database column session_data SqlType(LONGTEXT), Length(2147483647,true) */
    val sessionData: Rep[String] = column[String]("session_data", O.Length(2147483647,varying=true))
    /** Database column expire_date SqlType(DATETIME) */
    val expireDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("expire_date")

    /** Index over (expireDate) (database name django_session_expire_date_a5c62663) */
    val index1 = index("django_session_expire_date_a5c62663", expireDate)
  }
  /** Collection-like TableQuery object for table DjangoSession */
  lazy val DjangoSession = new TableQuery(tag => new DjangoSession(tag))

  /** Entity class storing rows of table Font
   *  @param fontSrl Database column font_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param fontFileName Database column font_file_name SqlType(VARCHAR), Length(50,true)
   *  @param fontFamilyName Database column font_family_name SqlType(VARCHAR), Length(50,true)
   *  @param fontStyle Database column font_style SqlType(VARCHAR), Length(10,true)
   *  @param fontWeight Database column font_weight SqlType(INT)
   *  @param fontLicenseSrl Database column font_license_srl SqlType(BIGINT)
   *  @param fontCopyrightSrl Database column font_copyright_srl SqlType(BIGINT)
   *  @param fontCreatorSrl Database column font_creator_srl SqlType(BIGINT)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class FontRow(fontSrl: Long, fontFileName: String, fontFamilyName: String, fontStyle: String, fontWeight: Int, fontLicenseSrl: Long, fontCopyrightSrl: Long, fontCreatorSrl: Long, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching FontRow objects using plain SQL queries */
  implicit def GetResultFontRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Int], e3: GR[java.sql.Timestamp]): GR[FontRow] = GR{
    prs => import prs._
    FontRow.tupled((<<[Long], <<[String], <<[String], <<[String], <<[Int], <<[Long], <<[Long], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table font. Objects of this class serve as prototypes for rows in queries. */
  class Font(_tableTag: Tag) extends profile.api.Table[FontRow](_tableTag, Some("rocket_font_main_db"), "font") {
    def * = (fontSrl, fontFileName, fontFamilyName, fontStyle, fontWeight, fontLicenseSrl, fontCopyrightSrl, fontCreatorSrl, created, modified) <> (FontRow.tupled, FontRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(fontSrl), Rep.Some(fontFileName), Rep.Some(fontFamilyName), Rep.Some(fontStyle), Rep.Some(fontWeight), Rep.Some(fontLicenseSrl), Rep.Some(fontCopyrightSrl), Rep.Some(fontCreatorSrl), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> FontRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column font_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val fontSrl: Rep[Long] = column[Long]("font_srl", O.AutoInc, O.PrimaryKey)
    /** Database column font_file_name SqlType(VARCHAR), Length(50,true) */
    val fontFileName: Rep[String] = column[String]("font_file_name", O.Length(50,varying=true))
    /** Database column font_family_name SqlType(VARCHAR), Length(50,true) */
    val fontFamilyName: Rep[String] = column[String]("font_family_name", O.Length(50,varying=true))
    /** Database column font_style SqlType(VARCHAR), Length(10,true) */
    val fontStyle: Rep[String] = column[String]("font_style", O.Length(10,varying=true))
    /** Database column font_weight SqlType(INT) */
    val fontWeight: Rep[Int] = column[Int]("font_weight")
    /** Database column font_license_srl SqlType(BIGINT) */
    val fontLicenseSrl: Rep[Long] = column[Long]("font_license_srl")
    /** Database column font_copyright_srl SqlType(BIGINT) */
    val fontCopyrightSrl: Rep[Long] = column[Long]("font_copyright_srl")
    /** Database column font_creator_srl SqlType(BIGINT) */
    val fontCreatorSrl: Rep[Long] = column[Long]("font_creator_srl")
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Foreign key referencing FontCopyright (database name font_font_copyright_font_copyright_srl_fk) */
    lazy val fontCopyrightFk = foreignKey("font_font_copyright_font_copyright_srl_fk", fontCopyrightSrl, FontCopyright)(r => r.fontCopyrightSrl, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing FontLicense (database name font_font_license_font_license_srl_fk) */
    lazy val fontLicenseFk = foreignKey("font_font_license_font_license_srl_fk", fontLicenseSrl, FontLicense)(r => r.fontLicenseSrl, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table Font */
  lazy val Font = new TableQuery(tag => new Font(tag))

  /** Entity class storing rows of table FontAccessLog
   *  @param fontAccessSrl Database column font_access_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param fontSrl Database column font_srl SqlType(BIGINT)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class FontAccessLogRow(fontAccessSrl: Long, fontSrl: Long, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching FontAccessLogRow objects using plain SQL queries */
  implicit def GetResultFontAccessLogRow(implicit e0: GR[Long], e1: GR[java.sql.Timestamp]): GR[FontAccessLogRow] = GR{
    prs => import prs._
    FontAccessLogRow.tupled((<<[Long], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table font_access_log. Objects of this class serve as prototypes for rows in queries. */
  class FontAccessLog(_tableTag: Tag) extends profile.api.Table[FontAccessLogRow](_tableTag, Some("rocket_font_main_db"), "font_access_log") {
    def * = (fontAccessSrl, fontSrl, created, modified) <> (FontAccessLogRow.tupled, FontAccessLogRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(fontAccessSrl), Rep.Some(fontSrl), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> FontAccessLogRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column font_access_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val fontAccessSrl: Rep[Long] = column[Long]("font_access_srl", O.AutoInc, O.PrimaryKey)
    /** Database column font_srl SqlType(BIGINT) */
    val fontSrl: Rep[Long] = column[Long]("font_srl")
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Foreign key referencing Font (database name font_access_log_font_font_srl_fk) */
    lazy val fontFk = foreignKey("font_access_log_font_font_srl_fk", fontSrl, Font)(r => r.fontSrl, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table FontAccessLog */
  lazy val FontAccessLog = new TableQuery(tag => new FontAccessLog(tag))

  /** Entity class storing rows of table FontCopyright
   *  @param fontCopyrightSrl Database column font_copyright_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param title Database column title SqlType(VARCHAR), Length(255,true)
   *  @param content Database column content SqlType(TEXT)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class FontCopyrightRow(fontCopyrightSrl: Long, title: String, content: String, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching FontCopyrightRow objects using plain SQL queries */
  implicit def GetResultFontCopyrightRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[FontCopyrightRow] = GR{
    prs => import prs._
    FontCopyrightRow.tupled((<<[Long], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table font_copyright. Objects of this class serve as prototypes for rows in queries. */
  class FontCopyright(_tableTag: Tag) extends profile.api.Table[FontCopyrightRow](_tableTag, Some("rocket_font_main_db"), "font_copyright") {
    def * = (fontCopyrightSrl, title, content, created, modified) <> (FontCopyrightRow.tupled, FontCopyrightRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(fontCopyrightSrl), Rep.Some(title), Rep.Some(content), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> FontCopyrightRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column font_copyright_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val fontCopyrightSrl: Rep[Long] = column[Long]("font_copyright_srl", O.AutoInc, O.PrimaryKey)
    /** Database column title SqlType(VARCHAR), Length(255,true) */
    val title: Rep[String] = column[String]("title", O.Length(255,varying=true))
    /** Database column content SqlType(TEXT) */
    val content: Rep[String] = column[String]("content")
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")
  }
  /** Collection-like TableQuery object for table FontCopyright */
  lazy val FontCopyright = new TableQuery(tag => new FontCopyright(tag))

  /** Entity class storing rows of table FontGroup
   *  @param fontGroupSrl Database column font_group_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param fontGroupName Database column font_group_name SqlType(VARCHAR), Length(50,true)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class FontGroupRow(fontGroupSrl: Long, fontGroupName: String, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching FontGroupRow objects using plain SQL queries */
  implicit def GetResultFontGroupRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[FontGroupRow] = GR{
    prs => import prs._
    FontGroupRow.tupled((<<[Long], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table font_group. Objects of this class serve as prototypes for rows in queries. */
  class FontGroup(_tableTag: Tag) extends profile.api.Table[FontGroupRow](_tableTag, Some("rocket_font_main_db"), "font_group") {
    def * = (fontGroupSrl, fontGroupName, created, modified) <> (FontGroupRow.tupled, FontGroupRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(fontGroupSrl), Rep.Some(fontGroupName), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> FontGroupRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column font_group_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val fontGroupSrl: Rep[Long] = column[Long]("font_group_srl", O.AutoInc, O.PrimaryKey)
    /** Database column font_group_name SqlType(VARCHAR), Length(50,true) */
    val fontGroupName: Rep[String] = column[String]("font_group_name", O.Length(50,varying=true))
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")
  }
  /** Collection-like TableQuery object for table FontGroup */
  lazy val FontGroup = new TableQuery(tag => new FontGroup(tag))

  /** Entity class storing rows of table FontGroupFont
   *  @param fontGroupFontSrl Database column font_group_font_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param fontGroup Database column font_group SqlType(BIGINT)
   *  @param fontSrl Database column font_srl SqlType(BIGINT)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class FontGroupFontRow(fontGroupFontSrl: Long, fontGroup: Long, fontSrl: Long, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching FontGroupFontRow objects using plain SQL queries */
  implicit def GetResultFontGroupFontRow(implicit e0: GR[Long], e1: GR[java.sql.Timestamp]): GR[FontGroupFontRow] = GR{
    prs => import prs._
    FontGroupFontRow.tupled((<<[Long], <<[Long], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table font_group_font. Objects of this class serve as prototypes for rows in queries. */
  class FontGroupFont(_tableTag: Tag) extends profile.api.Table[FontGroupFontRow](_tableTag, Some("rocket_font_main_db"), "font_group_font") {
    def * = (fontGroupFontSrl, fontGroup, fontSrl, created, modified) <> (FontGroupFontRow.tupled, FontGroupFontRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(fontGroupFontSrl), Rep.Some(fontGroup), Rep.Some(fontSrl), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> FontGroupFontRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column font_group_font_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val fontGroupFontSrl: Rep[Long] = column[Long]("font_group_font_srl", O.AutoInc, O.PrimaryKey)
    /** Database column font_group SqlType(BIGINT) */
    val fontGroup: Rep[Long] = column[Long]("font_group")
    /** Database column font_srl SqlType(BIGINT) */
    val fontSrl: Rep[Long] = column[Long]("font_srl")
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")
  }
  /** Collection-like TableQuery object for table FontGroupFont */
  lazy val FontGroupFont = new TableQuery(tag => new FontGroupFont(tag))

  /** Entity class storing rows of table FontLicense
   *  @param fontLicenseSrl Database column font_license_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param fontLicenseName Database column font_license_name SqlType(VARCHAR), Length(50,true)
   *  @param fontLicenseText Database column font_license_text SqlType(TEXT)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class FontLicenseRow(fontLicenseSrl: Long, fontLicenseName: String, fontLicenseText: String, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching FontLicenseRow objects using plain SQL queries */
  implicit def GetResultFontLicenseRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[FontLicenseRow] = GR{
    prs => import prs._
    FontLicenseRow.tupled((<<[Long], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table font_license. Objects of this class serve as prototypes for rows in queries. */
  class FontLicense(_tableTag: Tag) extends profile.api.Table[FontLicenseRow](_tableTag, Some("rocket_font_main_db"), "font_license") {
    def * = (fontLicenseSrl, fontLicenseName, fontLicenseText, created, modified) <> (FontLicenseRow.tupled, FontLicenseRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(fontLicenseSrl), Rep.Some(fontLicenseName), Rep.Some(fontLicenseText), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> FontLicenseRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column font_license_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val fontLicenseSrl: Rep[Long] = column[Long]("font_license_srl", O.AutoInc, O.PrimaryKey)
    /** Database column font_license_name SqlType(VARCHAR), Length(50,true) */
    val fontLicenseName: Rep[String] = column[String]("font_license_name", O.Length(50,varying=true))
    /** Database column font_license_text SqlType(TEXT) */
    val fontLicenseText: Rep[String] = column[String]("font_license_text")
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")
  }
  /** Collection-like TableQuery object for table FontLicense */
  lazy val FontLicense = new TableQuery(tag => new FontLicense(tag))

  /** Entity class storing rows of table FontPrice
   *  @param fontPriceSrl Database column font_price_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param fontSrl Database column font_srl SqlType(BIGINT)
   *  @param fontPricePerRequest Database column font_price_per_request SqlType(INT)
   *  @param fontComissionPerRequest Database column font_comission_per_request SqlType(INT)
   *  @param fontPricePerMinute Database column font_price_per_minute SqlType(INT)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class FontPriceRow(fontPriceSrl: Long, fontSrl: Long, fontPricePerRequest: Int, fontComissionPerRequest: Int, fontPricePerMinute: Int, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching FontPriceRow objects using plain SQL queries */
  implicit def GetResultFontPriceRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[java.sql.Timestamp]): GR[FontPriceRow] = GR{
    prs => import prs._
    FontPriceRow.tupled((<<[Long], <<[Long], <<[Int], <<[Int], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table font_price. Objects of this class serve as prototypes for rows in queries. */
  class FontPrice(_tableTag: Tag) extends profile.api.Table[FontPriceRow](_tableTag, Some("rocket_font_main_db"), "font_price") {
    def * = (fontPriceSrl, fontSrl, fontPricePerRequest, fontComissionPerRequest, fontPricePerMinute, created, modified) <> (FontPriceRow.tupled, FontPriceRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(fontPriceSrl), Rep.Some(fontSrl), Rep.Some(fontPricePerRequest), Rep.Some(fontComissionPerRequest), Rep.Some(fontPricePerMinute), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> FontPriceRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column font_price_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val fontPriceSrl: Rep[Long] = column[Long]("font_price_srl", O.AutoInc, O.PrimaryKey)
    /** Database column font_srl SqlType(BIGINT) */
    val fontSrl: Rep[Long] = column[Long]("font_srl")
    /** Database column font_price_per_request SqlType(INT) */
    val fontPricePerRequest: Rep[Int] = column[Int]("font_price_per_request")
    /** Database column font_comission_per_request SqlType(INT) */
    val fontComissionPerRequest: Rep[Int] = column[Int]("font_comission_per_request")
    /** Database column font_price_per_minute SqlType(INT) */
    val fontPricePerMinute: Rep[Int] = column[Int]("font_price_per_minute")
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Foreign key referencing Font (database name font_price_font_font_srl_fk) */
    lazy val fontFk = foreignKey("font_price_font_font_srl_fk", fontSrl, Font)(r => r.fontSrl, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)

    /** Uniqueness Index over (fontSrl) (database name font_price_font_srl_uindex) */
    val index1 = index("font_price_font_srl_uindex", fontSrl, unique=true)
  }
  /** Collection-like TableQuery object for table FontPrice */
  lazy val FontPrice = new TableQuery(tag => new FontPrice(tag))

  /** Entity class storing rows of table FontUnicode
   *  @param fontSrl Database column font_srl SqlType(BIGINT)
   *  @param fontUnicodeSrl Database column font_unicode_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param unicode Database column unicode SqlType(BIGINT)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class FontUnicodeRow(fontSrl: Long, fontUnicodeSrl: Long, unicode: Long, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching FontUnicodeRow objects using plain SQL queries */
  implicit def GetResultFontUnicodeRow(implicit e0: GR[Long], e1: GR[java.sql.Timestamp]): GR[FontUnicodeRow] = GR{
    prs => import prs._
    FontUnicodeRow.tupled((<<[Long], <<[Long], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table font_unicode. Objects of this class serve as prototypes for rows in queries. */
  class FontUnicode(_tableTag: Tag) extends profile.api.Table[FontUnicodeRow](_tableTag, Some("rocket_font_main_db"), "font_unicode") {
    def * = (fontSrl, fontUnicodeSrl, unicode, created, modified) <> (FontUnicodeRow.tupled, FontUnicodeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(fontSrl), Rep.Some(fontUnicodeSrl), Rep.Some(unicode), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> FontUnicodeRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column font_srl SqlType(BIGINT) */
    val fontSrl: Rep[Long] = column[Long]("font_srl")
    /** Database column font_unicode_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val fontUnicodeSrl: Rep[Long] = column[Long]("font_unicode_srl", O.AutoInc, O.PrimaryKey)
    /** Database column unicode SqlType(BIGINT) */
    val unicode: Rep[Long] = column[Long]("unicode")
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Foreign key referencing Font (database name font_glyphe_font_font_srl_fk) */
    lazy val fontFk = foreignKey("font_glyphe_font_font_srl_fk", fontSrl, Font)(r => r.fontSrl, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)

    /** Uniqueness Index over (fontSrl,unicode) (database name font_unicode_uk) */
    val index1 = index("font_unicode_uk", (fontSrl, unicode), unique=true)
  }
  /** Collection-like TableQuery object for table FontUnicode */
  lazy val FontUnicode = new TableQuery(tag => new FontUnicode(tag))

  /** Entity class storing rows of table FontUnicodeSetC
   *  @param setCSrl Database column set_c_srl SqlType(INT), AutoInc, PrimaryKey
   *  @param unicode Database column unicode SqlType(INT)
   *  @param priority Database column priority SqlType(BIGINT)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class FontUnicodeSetCRow(setCSrl: Int, unicode: Int, priority: Long, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching FontUnicodeSetCRow objects using plain SQL queries */
  implicit def GetResultFontUnicodeSetCRow(implicit e0: GR[Int], e1: GR[Long], e2: GR[java.sql.Timestamp]): GR[FontUnicodeSetCRow] = GR{
    prs => import prs._
    FontUnicodeSetCRow.tupled((<<[Int], <<[Int], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table font_unicode_set_c. Objects of this class serve as prototypes for rows in queries. */
  class FontUnicodeSetC(_tableTag: Tag) extends profile.api.Table[FontUnicodeSetCRow](_tableTag, Some("rocket_font_main_db"), "font_unicode_set_c") {
    def * = (setCSrl, unicode, priority, created, modified) <> (FontUnicodeSetCRow.tupled, FontUnicodeSetCRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(setCSrl), Rep.Some(unicode), Rep.Some(priority), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> FontUnicodeSetCRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column set_c_srl SqlType(INT), AutoInc, PrimaryKey */
    val setCSrl: Rep[Int] = column[Int]("set_c_srl", O.AutoInc, O.PrimaryKey)
    /** Database column unicode SqlType(INT) */
    val unicode: Rep[Int] = column[Int]("unicode")
    /** Database column priority SqlType(BIGINT) */
    val priority: Rep[Long] = column[Long]("priority")
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Uniqueness Index over (unicode) (database name font_unicode_set_c_unicode_uindex) */
    val index1 = index("font_unicode_set_c_unicode_uindex", unicode, unique=true)
  }
  /** Collection-like TableQuery object for table FontUnicodeSetC */
  lazy val FontUnicodeSetC = new TableQuery(tag => new FontUnicodeSetC(tag))

  /** Entity class storing rows of table FontUnicodeSetCBak
   *  @param setCSrl Database column set_c_srl SqlType(INT), AutoInc, PrimaryKey
   *  @param unicode Database column unicode SqlType(INT)
   *  @param priority Database column priority SqlType(BIGINT)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class FontUnicodeSetCBakRow(setCSrl: Int, unicode: Int, priority: Long, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching FontUnicodeSetCBakRow objects using plain SQL queries */
  implicit def GetResultFontUnicodeSetCBakRow(implicit e0: GR[Int], e1: GR[Long], e2: GR[java.sql.Timestamp]): GR[FontUnicodeSetCBakRow] = GR{
    prs => import prs._
    FontUnicodeSetCBakRow.tupled((<<[Int], <<[Int], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table font_unicode_set_c_bak. Objects of this class serve as prototypes for rows in queries. */
  class FontUnicodeSetCBak(_tableTag: Tag) extends profile.api.Table[FontUnicodeSetCBakRow](_tableTag, Some("rocket_font_main_db"), "font_unicode_set_c_bak") {
    def * = (setCSrl, unicode, priority, created, modified) <> (FontUnicodeSetCBakRow.tupled, FontUnicodeSetCBakRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(setCSrl), Rep.Some(unicode), Rep.Some(priority), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> FontUnicodeSetCBakRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column set_c_srl SqlType(INT), AutoInc, PrimaryKey */
    val setCSrl: Rep[Int] = column[Int]("set_c_srl", O.AutoInc, O.PrimaryKey)
    /** Database column unicode SqlType(INT) */
    val unicode: Rep[Int] = column[Int]("unicode")
    /** Database column priority SqlType(BIGINT) */
    val priority: Rep[Long] = column[Long]("priority")
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Uniqueness Index over (unicode) (database name font_unicode_set_c_unicode_uindex) */
    val index1 = index("font_unicode_set_c_unicode_uindex", unicode, unique=true)
  }
  /** Collection-like TableQuery object for table FontUnicodeSetCBak */
  lazy val FontUnicodeSetCBak = new TableQuery(tag => new FontUnicodeSetCBak(tag))

  /** Entity class storing rows of table FontUsageMeasureAccessLog
   *  @param fontSrl Database column font_srl SqlType(BIGINT)
   *  @param fontUsageMeasureAccessSrl Database column font_usage_measure_access_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param protocol Database column protocol SqlType(VARCHAR), Length(5,true)
   *  @param host Database column host SqlType(VARCHAR), Length(255,true)
   *  @param port Database column port SqlType(INT)
   *  @param path Database column path SqlType(VARCHAR), Length(255,true)
   *  @param session Database column session SqlType(VARCHAR), Length(64,true)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class FontUsageMeasureAccessLogRow(fontSrl: Long, fontUsageMeasureAccessSrl: Long, protocol: String, host: String, port: Int, path: String, session: String, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching FontUsageMeasureAccessLogRow objects using plain SQL queries */
  implicit def GetResultFontUsageMeasureAccessLogRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Int], e3: GR[java.sql.Timestamp]): GR[FontUsageMeasureAccessLogRow] = GR{
    prs => import prs._
    FontUsageMeasureAccessLogRow.tupled((<<[Long], <<[Long], <<[String], <<[String], <<[Int], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table font_usage_measure_access_log. Objects of this class serve as prototypes for rows in queries. */
  class FontUsageMeasureAccessLog(_tableTag: Tag) extends profile.api.Table[FontUsageMeasureAccessLogRow](_tableTag, Some("rocket_font_main_db"), "font_usage_measure_access_log") {
    def * = (fontSrl, fontUsageMeasureAccessSrl, protocol, host, port, path, session, created, modified) <> (FontUsageMeasureAccessLogRow.tupled, FontUsageMeasureAccessLogRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(fontSrl), Rep.Some(fontUsageMeasureAccessSrl), Rep.Some(protocol), Rep.Some(host), Rep.Some(port), Rep.Some(path), Rep.Some(session), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> FontUsageMeasureAccessLogRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column font_srl SqlType(BIGINT) */
    val fontSrl: Rep[Long] = column[Long]("font_srl")
    /** Database column font_usage_measure_access_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val fontUsageMeasureAccessSrl: Rep[Long] = column[Long]("font_usage_measure_access_srl", O.AutoInc, O.PrimaryKey)
    /** Database column protocol SqlType(VARCHAR), Length(5,true) */
    val protocol: Rep[String] = column[String]("protocol", O.Length(5,varying=true))
    /** Database column host SqlType(VARCHAR), Length(255,true) */
    val host: Rep[String] = column[String]("host", O.Length(255,varying=true))
    /** Database column port SqlType(INT) */
    val port: Rep[Int] = column[Int]("port")
    /** Database column path SqlType(VARCHAR), Length(255,true) */
    val path: Rep[String] = column[String]("path", O.Length(255,varying=true))
    /** Database column session SqlType(VARCHAR), Length(64,true) */
    val session: Rep[String] = column[String]("session", O.Length(64,varying=true))
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Foreign key referencing Font (database name font_usage_measure_access_log_font_font_srl_fk) */
    lazy val fontFk = foreignKey("font_usage_measure_access_log_font_font_srl_fk", fontSrl, Font)(r => r.fontSrl, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)

    /** Uniqueness Index over (session) (database name font_usage_measure_access_log_session_uindex) */
    val index1 = index("font_usage_measure_access_log_session_uindex", session, unique=true)
  }
  /** Collection-like TableQuery object for table FontUsageMeasureAccessLog */
  lazy val FontUsageMeasureAccessLog = new TableQuery(tag => new FontUsageMeasureAccessLog(tag))

  /** Entity class storing rows of table Group
   *  @param groupSrl Database column group_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param groupName Database column group_name SqlType(VARCHAR), Length(50,true)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class GroupRow(groupSrl: Long, groupName: String, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching GroupRow objects using plain SQL queries */
  implicit def GetResultGroupRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[GroupRow] = GR{
    prs => import prs._
    GroupRow.tupled((<<[Long], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table group. Objects of this class serve as prototypes for rows in queries. */
  class Group(_tableTag: Tag) extends profile.api.Table[GroupRow](_tableTag, Some("rocket_font_main_db"), "group") {
    def * = (groupSrl, groupName, created, modified) <> (GroupRow.tupled, GroupRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(groupSrl), Rep.Some(groupName), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> GroupRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column group_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val groupSrl: Rep[Long] = column[Long]("group_srl", O.AutoInc, O.PrimaryKey)
    /** Database column group_name SqlType(VARCHAR), Length(50,true) */
    val groupName: Rep[String] = column[String]("group_name", O.Length(50,varying=true))
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")
  }
  /** Collection-like TableQuery object for table Group */
  lazy val Group = new TableQuery(tag => new Group(tag))

  /** Entity class storing rows of table Member
   *  @param memberSrl Database column member_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param email Database column email SqlType(VARCHAR), Length(255,true)
   *  @param password Database column password SqlType(VARCHAR), Length(128,true)
   *  @param accountValidStatus Database column account_valid_status SqlType(VARCHAR), Length(10,true)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class MemberRow(memberSrl: Long, email: String, password: String, accountValidStatus: String, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching MemberRow objects using plain SQL queries */
  implicit def GetResultMemberRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[MemberRow] = GR{
    prs => import prs._
    MemberRow.tupled((<<[Long], <<[String], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table member. Objects of this class serve as prototypes for rows in queries. */
  class Member(_tableTag: Tag) extends profile.api.Table[MemberRow](_tableTag, Some("rocket_font_main_db"), "member") {
    def * = (memberSrl, email, password, accountValidStatus, created, modified) <> (MemberRow.tupled, MemberRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(memberSrl), Rep.Some(email), Rep.Some(password), Rep.Some(accountValidStatus), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> MemberRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column member_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val memberSrl: Rep[Long] = column[Long]("member_srl", O.AutoInc, O.PrimaryKey)
    /** Database column email SqlType(VARCHAR), Length(255,true) */
    val email: Rep[String] = column[String]("email", O.Length(255,varying=true))
    /** Database column password SqlType(VARCHAR), Length(128,true) */
    val password: Rep[String] = column[String]("password", O.Length(128,varying=true))
    /** Database column account_valid_status SqlType(VARCHAR), Length(10,true) */
    val accountValidStatus: Rep[String] = column[String]("account_valid_status", O.Length(10,varying=true))
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Uniqueness Index over (email) (database name member_email_uindex) */
    val index1 = index("member_email_uindex", email, unique=true)
  }
  /** Collection-like TableQuery object for table Member */
  lazy val Member = new TableQuery(tag => new Member(tag))

  /** Entity class storing rows of table MemberCreditcard
   *  @param creditCardSrl Database column credit_card_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param memberSrl Database column member_srl SqlType(BIGINT)
   *  @param billingKey Database column billing_key SqlType(VARCHAR), Length(64,true)
   *  @param cardNickname Database column card_nickname SqlType(VARCHAR), Length(12,true)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class MemberCreditcardRow(creditCardSrl: Long, memberSrl: Long, billingKey: String, cardNickname: String, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching MemberCreditcardRow objects using plain SQL queries */
  implicit def GetResultMemberCreditcardRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[MemberCreditcardRow] = GR{
    prs => import prs._
    MemberCreditcardRow.tupled((<<[Long], <<[Long], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table member_creditcard. Objects of this class serve as prototypes for rows in queries. */
  class MemberCreditcard(_tableTag: Tag) extends profile.api.Table[MemberCreditcardRow](_tableTag, Some("rocket_font_main_db"), "member_creditcard") {
    def * = (creditCardSrl, memberSrl, billingKey, cardNickname, created, modified) <> (MemberCreditcardRow.tupled, MemberCreditcardRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(creditCardSrl), Rep.Some(memberSrl), Rep.Some(billingKey), Rep.Some(cardNickname), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> MemberCreditcardRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column credit_card_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val creditCardSrl: Rep[Long] = column[Long]("credit_card_srl", O.AutoInc, O.PrimaryKey)
    /** Database column member_srl SqlType(BIGINT) */
    val memberSrl: Rep[Long] = column[Long]("member_srl")
    /** Database column billing_key SqlType(VARCHAR), Length(64,true) */
    val billingKey: Rep[String] = column[String]("billing_key", O.Length(64,varying=true))
    /** Database column card_nickname SqlType(VARCHAR), Length(12,true) */
    val cardNickname: Rep[String] = column[String]("card_nickname", O.Length(12,varying=true))
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Uniqueness Index over (memberSrl) (database name member_creditcard_member_srl_uindex) */
    val index1 = index("member_creditcard_member_srl_uindex", memberSrl, unique=true)
  }
  /** Collection-like TableQuery object for table MemberCreditcard */
  lazy val MemberCreditcard = new TableQuery(tag => new MemberCreditcard(tag))

  /** Entity class storing rows of table MemberEmailAuth
   *  @param memberEmailAuthSrl Database column member_email_auth_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param memberSrl Database column member_srl SqlType(BIGINT)
   *  @param token Database column token SqlType(VARCHAR), Length(64,true)
   *  @param expire Database column expire SqlType(DATETIME)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class MemberEmailAuthRow(memberEmailAuthSrl: Long, memberSrl: Long, token: String, expire: java.sql.Timestamp, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching MemberEmailAuthRow objects using plain SQL queries */
  implicit def GetResultMemberEmailAuthRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[MemberEmailAuthRow] = GR{
    prs => import prs._
    MemberEmailAuthRow.tupled((<<[Long], <<[Long], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table member_email_auth. Objects of this class serve as prototypes for rows in queries. */
  class MemberEmailAuth(_tableTag: Tag) extends profile.api.Table[MemberEmailAuthRow](_tableTag, Some("rocket_font_main_db"), "member_email_auth") {
    def * = (memberEmailAuthSrl, memberSrl, token, expire, created, modified) <> (MemberEmailAuthRow.tupled, MemberEmailAuthRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(memberEmailAuthSrl), Rep.Some(memberSrl), Rep.Some(token), Rep.Some(expire), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> MemberEmailAuthRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column member_email_auth_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val memberEmailAuthSrl: Rep[Long] = column[Long]("member_email_auth_srl", O.AutoInc, O.PrimaryKey)
    /** Database column member_srl SqlType(BIGINT) */
    val memberSrl: Rep[Long] = column[Long]("member_srl")
    /** Database column token SqlType(VARCHAR), Length(64,true) */
    val token: Rep[String] = column[String]("token", O.Length(64,varying=true))
    /** Database column expire SqlType(DATETIME) */
    val expire: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("expire")
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Foreign key referencing Member (database name member_email_auth_member_member_srl_fk) */
    lazy val memberFk = foreignKey("member_email_auth_member_member_srl_fk", memberSrl, Member)(r => r.memberSrl, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table MemberEmailAuth */
  lazy val MemberEmailAuth = new TableQuery(tag => new MemberEmailAuth(tag))

  /** Entity class storing rows of table MemberFindPassword
   *  @param memberFindPasswordSrl Database column member_find_password_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param memberSrl Database column member_srl SqlType(BIGINT)
   *  @param token Database column token SqlType(VARCHAR), Length(64,true)
   *  @param expires Database column expires SqlType(DATETIME)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class MemberFindPasswordRow(memberFindPasswordSrl: Long, memberSrl: Long, token: String, expires: java.sql.Timestamp, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching MemberFindPasswordRow objects using plain SQL queries */
  implicit def GetResultMemberFindPasswordRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[MemberFindPasswordRow] = GR{
    prs => import prs._
    MemberFindPasswordRow.tupled((<<[Long], <<[Long], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table member_find_password. Objects of this class serve as prototypes for rows in queries. */
  class MemberFindPassword(_tableTag: Tag) extends profile.api.Table[MemberFindPasswordRow](_tableTag, Some("rocket_font_main_db"), "member_find_password") {
    def * = (memberFindPasswordSrl, memberSrl, token, expires, created, modified) <> (MemberFindPasswordRow.tupled, MemberFindPasswordRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(memberFindPasswordSrl), Rep.Some(memberSrl), Rep.Some(token), Rep.Some(expires), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> MemberFindPasswordRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column member_find_password_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val memberFindPasswordSrl: Rep[Long] = column[Long]("member_find_password_srl", O.AutoInc, O.PrimaryKey)
    /** Database column member_srl SqlType(BIGINT) */
    val memberSrl: Rep[Long] = column[Long]("member_srl")
    /** Database column token SqlType(VARCHAR), Length(64,true) */
    val token: Rep[String] = column[String]("token", O.Length(64,varying=true))
    /** Database column expires SqlType(DATETIME) */
    val expires: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("expires")
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Foreign key referencing Member (database name member_find_password_member_member_srl_fk) */
    lazy val memberFk = foreignKey("member_find_password_member_member_srl_fk", memberSrl, Member)(r => r.memberSrl, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table MemberFindPassword */
  lazy val MemberFindPassword = new TableQuery(tag => new MemberFindPassword(tag))

  /** Entity class storing rows of table MemberFontCopyright
   *  @param memberFontCopyrightSrl Database column member_font_copyright_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param memberSrl Database column member_srl SqlType(BIGINT)
   *  @param fontCopyrightSrl Database column font_copyright_srl SqlType(BIGINT)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class MemberFontCopyrightRow(memberFontCopyrightSrl: Long, memberSrl: Long, fontCopyrightSrl: Long, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching MemberFontCopyrightRow objects using plain SQL queries */
  implicit def GetResultMemberFontCopyrightRow(implicit e0: GR[Long], e1: GR[java.sql.Timestamp]): GR[MemberFontCopyrightRow] = GR{
    prs => import prs._
    MemberFontCopyrightRow.tupled((<<[Long], <<[Long], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table member_font_copyright. Objects of this class serve as prototypes for rows in queries. */
  class MemberFontCopyright(_tableTag: Tag) extends profile.api.Table[MemberFontCopyrightRow](_tableTag, Some("rocket_font_main_db"), "member_font_copyright") {
    def * = (memberFontCopyrightSrl, memberSrl, fontCopyrightSrl, created, modified) <> (MemberFontCopyrightRow.tupled, MemberFontCopyrightRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(memberFontCopyrightSrl), Rep.Some(memberSrl), Rep.Some(fontCopyrightSrl), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> MemberFontCopyrightRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column member_font_copyright_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val memberFontCopyrightSrl: Rep[Long] = column[Long]("member_font_copyright_srl", O.AutoInc, O.PrimaryKey)
    /** Database column member_srl SqlType(BIGINT) */
    val memberSrl: Rep[Long] = column[Long]("member_srl")
    /** Database column font_copyright_srl SqlType(BIGINT) */
    val fontCopyrightSrl: Rep[Long] = column[Long]("font_copyright_srl")
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")
  }
  /** Collection-like TableQuery object for table MemberFontCopyright */
  lazy val MemberFontCopyright = new TableQuery(tag => new MemberFontCopyright(tag))

  /** Entity class storing rows of table MemberGroup
   *  @param memberGroupSrl Database column member_group_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param memberSrl Database column member_srl SqlType(BIGINT)
   *  @param groupSrl Database column group_srl SqlType(BIGINT)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class MemberGroupRow(memberGroupSrl: Long, memberSrl: Long, groupSrl: Long, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching MemberGroupRow objects using plain SQL queries */
  implicit def GetResultMemberGroupRow(implicit e0: GR[Long], e1: GR[java.sql.Timestamp]): GR[MemberGroupRow] = GR{
    prs => import prs._
    MemberGroupRow.tupled((<<[Long], <<[Long], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table member_group. Objects of this class serve as prototypes for rows in queries. */
  class MemberGroup(_tableTag: Tag) extends profile.api.Table[MemberGroupRow](_tableTag, Some("rocket_font_main_db"), "member_group") {
    def * = (memberGroupSrl, memberSrl, groupSrl, created, modified) <> (MemberGroupRow.tupled, MemberGroupRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(memberGroupSrl), Rep.Some(memberSrl), Rep.Some(groupSrl), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> MemberGroupRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column member_group_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val memberGroupSrl: Rep[Long] = column[Long]("member_group_srl", O.AutoInc, O.PrimaryKey)
    /** Database column member_srl SqlType(BIGINT) */
    val memberSrl: Rep[Long] = column[Long]("member_srl")
    /** Database column group_srl SqlType(BIGINT) */
    val groupSrl: Rep[Long] = column[Long]("group_srl")
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Foreign key referencing Group (database name member_group_group_group_srl_fk) */
    lazy val groupFk = foreignKey("member_group_group_group_srl_fk", groupSrl, Group)(r => r.groupSrl, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing Member (database name member_group_member_member_srl_fk) */
    lazy val memberFk = foreignKey("member_group_member_member_srl_fk", memberSrl, Member)(r => r.memberSrl, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)

    /** Uniqueness Index over (memberSrl,groupSrl) (database name member_group_pk) */
    val index1 = index("member_group_pk", (memberSrl, groupSrl), unique=true)
  }
  /** Collection-like TableQuery object for table MemberGroup */
  lazy val MemberGroup = new TableQuery(tag => new MemberGroup(tag))

  /** Entity class storing rows of table RegisteredHostname
   *  @param registredHostSrl Database column registred_host_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param memberSrl Database column member_srl SqlType(BIGINT)
   *  @param hostname Database column hostname SqlType(VARCHAR), Length(255,true)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class RegisteredHostnameRow(registredHostSrl: Long, memberSrl: Long, hostname: String, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching RegisteredHostnameRow objects using plain SQL queries */
  implicit def GetResultRegisteredHostnameRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[RegisteredHostnameRow] = GR{
    prs => import prs._
    RegisteredHostnameRow.tupled((<<[Long], <<[Long], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table registered_hostname. Objects of this class serve as prototypes for rows in queries. */
  class RegisteredHostname(_tableTag: Tag) extends profile.api.Table[RegisteredHostnameRow](_tableTag, Some("rocket_font_main_db"), "registered_hostname") {
    def * = (registredHostSrl, memberSrl, hostname, created, modified) <> (RegisteredHostnameRow.tupled, RegisteredHostnameRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(registredHostSrl), Rep.Some(memberSrl), Rep.Some(hostname), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> RegisteredHostnameRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column registred_host_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val registredHostSrl: Rep[Long] = column[Long]("registred_host_srl", O.AutoInc, O.PrimaryKey)
    /** Database column member_srl SqlType(BIGINT) */
    val memberSrl: Rep[Long] = column[Long]("member_srl")
    /** Database column hostname SqlType(VARCHAR), Length(255,true) */
    val hostname: Rep[String] = column[String]("hostname", O.Length(255,varying=true))
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Foreign key referencing Member (database name registered_domain_member_member_srl_fk) */
    lazy val memberFk = foreignKey("registered_domain_member_member_srl_fk", memberSrl, Member)(r => r.memberSrl, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)

    /** Uniqueness Index over (hostname) (database name registered_hostname_hostname_uindex) */
    val index1 = index("registered_hostname_hostname_uindex", hostname, unique=true)
  }
  /** Collection-like TableQuery object for table RegisteredHostname */
  lazy val RegisteredHostname = new TableQuery(tag => new RegisteredHostname(tag))

  /** Entity class storing rows of table RegisteredHostnamePending
   *  @param pendingHostnameSrl Database column pending_hostname_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param memberSrl Database column member_srl SqlType(BIGINT)
   *  @param hostname Database column hostname SqlType(VARCHAR), Length(255,true)
   *  @param dnsTxtRecord Database column dns_txt_record SqlType(VARCHAR), Length(255,true)
   *  @param expires Database column expires SqlType(DATETIME)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class RegisteredHostnamePendingRow(pendingHostnameSrl: Long, memberSrl: Long, hostname: String, dnsTxtRecord: String, expires: java.sql.Timestamp, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching RegisteredHostnamePendingRow objects using plain SQL queries */
  implicit def GetResultRegisteredHostnamePendingRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[RegisteredHostnamePendingRow] = GR{
    prs => import prs._
    RegisteredHostnamePendingRow.tupled((<<[Long], <<[Long], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table registered_hostname_pending. Objects of this class serve as prototypes for rows in queries. */
  class RegisteredHostnamePending(_tableTag: Tag) extends profile.api.Table[RegisteredHostnamePendingRow](_tableTag, Some("rocket_font_main_db"), "registered_hostname_pending") {
    def * = (pendingHostnameSrl, memberSrl, hostname, dnsTxtRecord, expires, created, modified) <> (RegisteredHostnamePendingRow.tupled, RegisteredHostnamePendingRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(pendingHostnameSrl), Rep.Some(memberSrl), Rep.Some(hostname), Rep.Some(dnsTxtRecord), Rep.Some(expires), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> RegisteredHostnamePendingRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column pending_hostname_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val pendingHostnameSrl: Rep[Long] = column[Long]("pending_hostname_srl", O.AutoInc, O.PrimaryKey)
    /** Database column member_srl SqlType(BIGINT) */
    val memberSrl: Rep[Long] = column[Long]("member_srl")
    /** Database column hostname SqlType(VARCHAR), Length(255,true) */
    val hostname: Rep[String] = column[String]("hostname", O.Length(255,varying=true))
    /** Database column dns_txt_record SqlType(VARCHAR), Length(255,true) */
    val dnsTxtRecord: Rep[String] = column[String]("dns_txt_record", O.Length(255,varying=true))
    /** Database column expires SqlType(DATETIME) */
    val expires: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("expires")
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Foreign key referencing Member (database name registered_hostname_pending_member_member_srl_fk) */
    lazy val memberFk = foreignKey("registered_hostname_pending_member_member_srl_fk", memberSrl, Member)(r => r.memberSrl, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table RegisteredHostnamePending */
  lazy val RegisteredHostnamePending = new TableQuery(tag => new RegisteredHostnamePending(tag))

  /** Entity class storing rows of table RocketFontTest
   *  @param srl Database column srl SqlType(INT), AutoInc, PrimaryKey
   *  @param url Database column url SqlType(VARCHAR), Length(255,true)
   *  @param text Database column text SqlType(LONGTEXT), Length(2147483647,true) */
  case class RocketFontTestRow(srl: Int, url: String, text: String)
  /** GetResult implicit for fetching RocketFontTestRow objects using plain SQL queries */
  implicit def GetResultRocketFontTestRow(implicit e0: GR[Int], e1: GR[String]): GR[RocketFontTestRow] = GR{
    prs => import prs._
    RocketFontTestRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table rocket_font_test. Objects of this class serve as prototypes for rows in queries. */
  class RocketFontTest(_tableTag: Tag) extends profile.api.Table[RocketFontTestRow](_tableTag, Some("rocket_font_main_db"), "rocket_font_test") {
    def * = (srl, url, text) <> (RocketFontTestRow.tupled, RocketFontTestRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(srl), Rep.Some(url), Rep.Some(text))).shaped.<>({r=>import r._; _1.map(_=> RocketFontTestRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column srl SqlType(INT), AutoInc, PrimaryKey */
    val srl: Rep[Int] = column[Int]("srl", O.AutoInc, O.PrimaryKey)
    /** Database column url SqlType(VARCHAR), Length(255,true) */
    val url: Rep[String] = column[String]("url", O.Length(255,varying=true))
    /** Database column text SqlType(LONGTEXT), Length(2147483647,true) */
    val text: Rep[String] = column[String]("text", O.Length(2147483647,varying=true))

    /** Uniqueness Index over (url) (database name rocket_font_test_url_uindex) */
    val index1 = index("rocket_font_test_url_uindex", url, unique=true)
  }
  /** Collection-like TableQuery object for table RocketFontTest */
  lazy val RocketFontTest = new TableQuery(tag => new RocketFontTest(tag))

  /** Entity class storing rows of table RocketFontTestResult
   *  @param srl Database column srl SqlType(INT), AutoInc, PrimaryKey
   *  @param fontFamily Database column font_family SqlType(VARCHAR), Length(50,true)
   *  @param fontAccessType Database column font_access_type SqlType(ENUM), Length(10,true)
   *  @param txbytes Database column txBytes SqlType(INT)
   *  @param url Database column url SqlType(VARCHAR), Length(255,true)
   *  @param created Database column created SqlType(DATETIME) */
  case class RocketFontTestResultRow(srl: Int, fontFamily: String, fontAccessType: String, txbytes: Int, url: String, created: java.sql.Timestamp)
  /** GetResult implicit for fetching RocketFontTestResultRow objects using plain SQL queries */
  implicit def GetResultRocketFontTestResultRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[RocketFontTestResultRow] = GR{
    prs => import prs._
    RocketFontTestResultRow.tupled((<<[Int], <<[String], <<[String], <<[Int], <<[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table rocket_font_test_result. Objects of this class serve as prototypes for rows in queries. */
  class RocketFontTestResult(_tableTag: Tag) extends profile.api.Table[RocketFontTestResultRow](_tableTag, Some("rocket_font_main_db"), "rocket_font_test_result") {
    def * = (srl, fontFamily, fontAccessType, txbytes, url, created) <> (RocketFontTestResultRow.tupled, RocketFontTestResultRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(srl), Rep.Some(fontFamily), Rep.Some(fontAccessType), Rep.Some(txbytes), Rep.Some(url), Rep.Some(created))).shaped.<>({r=>import r._; _1.map(_=> RocketFontTestResultRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column srl SqlType(INT), AutoInc, PrimaryKey */
    val srl: Rep[Int] = column[Int]("srl", O.AutoInc, O.PrimaryKey)
    /** Database column font_family SqlType(VARCHAR), Length(50,true) */
    val fontFamily: Rep[String] = column[String]("font_family", O.Length(50,varying=true))
    /** Database column font_access_type SqlType(ENUM), Length(10,true) */
    val fontAccessType: Rep[String] = column[String]("font_access_type", O.Length(10,varying=true))
    /** Database column txBytes SqlType(INT) */
    val txbytes: Rep[Int] = column[Int]("txBytes")
    /** Database column url SqlType(VARCHAR), Length(255,true) */
    val url: Rep[String] = column[String]("url", O.Length(255,varying=true))
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")

    /** Uniqueness Index over (url) (database name rocket_font_test_result_url_uindex) */
    val index1 = index("rocket_font_test_result_url_uindex", url, unique=true)
  }
  /** Collection-like TableQuery object for table RocketFontTestResult */
  lazy val RocketFontTestResult = new TableQuery(tag => new RocketFontTestResult(tag))

  /** Entity class storing rows of table UrlAccessLog
   *  @param urlAccessSrl Database column url_access_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param urlSrl Database column url_srl SqlType(BIGINT)
   *  @param created Database column created SqlType(DATETIME), Default(None)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class UrlAccessLogRow(urlAccessSrl: Long, urlSrl: Long, created: Option[java.sql.Timestamp] = None, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching UrlAccessLogRow objects using plain SQL queries */
  implicit def GetResultUrlAccessLogRow(implicit e0: GR[Long], e1: GR[Option[java.sql.Timestamp]], e2: GR[java.sql.Timestamp]): GR[UrlAccessLogRow] = GR{
    prs => import prs._
    UrlAccessLogRow.tupled((<<[Long], <<[Long], <<?[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table url_access_log. Objects of this class serve as prototypes for rows in queries. */
  class UrlAccessLog(_tableTag: Tag) extends profile.api.Table[UrlAccessLogRow](_tableTag, Some("rocket_font_main_db"), "url_access_log") {
    def * = (urlAccessSrl, urlSrl, created, modified) <> (UrlAccessLogRow.tupled, UrlAccessLogRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(urlAccessSrl), Rep.Some(urlSrl), created, Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> UrlAccessLogRow.tupled((_1.get, _2.get, _3, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column url_access_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val urlAccessSrl: Rep[Long] = column[Long]("url_access_srl", O.AutoInc, O.PrimaryKey)
    /** Database column url_srl SqlType(BIGINT) */
    val urlSrl: Rep[Long] = column[Long]("url_srl")
    /** Database column created SqlType(DATETIME), Default(None) */
    val created: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("created", O.Default(None))
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Foreign key referencing Urls (database name url_access_log_urls_url_srl_fk) */
    lazy val urlsFk = foreignKey("url_access_log_urls_url_srl_fk", urlSrl, Urls)(r => r.urlSrl, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table UrlAccessLog */
  lazy val UrlAccessLog = new TableQuery(tag => new UrlAccessLog(tag))

  /** Entity class storing rows of table UrlLetterLog
   *  @param urlLetterSrl Database column url_letter_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param urlAccessSrl Database column url_access_srl SqlType(BIGINT)
   *  @param unicode Database column unicode SqlType(BIGINT)
   *  @param count Database column count SqlType(INT)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class UrlLetterLogRow(urlLetterSrl: Long, urlAccessSrl: Long, unicode: Long, count: Int, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching UrlLetterLogRow objects using plain SQL queries */
  implicit def GetResultUrlLetterLogRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[java.sql.Timestamp]): GR[UrlLetterLogRow] = GR{
    prs => import prs._
    UrlLetterLogRow.tupled((<<[Long], <<[Long], <<[Long], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table url_letter_log. Objects of this class serve as prototypes for rows in queries. */
  class UrlLetterLog(_tableTag: Tag) extends profile.api.Table[UrlLetterLogRow](_tableTag, Some("rocket_font_main_db"), "url_letter_log") {
    def * = (urlLetterSrl, urlAccessSrl, unicode, count, created, modified) <> (UrlLetterLogRow.tupled, UrlLetterLogRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(urlLetterSrl), Rep.Some(urlAccessSrl), Rep.Some(unicode), Rep.Some(count), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> UrlLetterLogRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column url_letter_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val urlLetterSrl: Rep[Long] = column[Long]("url_letter_srl", O.AutoInc, O.PrimaryKey)
    /** Database column url_access_srl SqlType(BIGINT) */
    val urlAccessSrl: Rep[Long] = column[Long]("url_access_srl")
    /** Database column unicode SqlType(BIGINT) */
    val unicode: Rep[Long] = column[Long]("unicode")
    /** Database column count SqlType(INT) */
    val count: Rep[Int] = column[Int]("count")
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Foreign key referencing UrlAccessLog (database name url_letter_log_url_access_log_url_access_srl_fk) */
    lazy val urlAccessLogFk = foreignKey("url_letter_log_url_access_log_url_access_srl_fk", urlAccessSrl, UrlAccessLog)(r => r.urlAccessSrl, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table UrlLetterLog */
  lazy val UrlLetterLog = new TableQuery(tag => new UrlLetterLog(tag))

  /** Entity class storing rows of table Urls
   *  @param urlSrl Database column url_srl SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param url Database column url SqlType(VARCHAR), Length(255,true)
   *  @param created Database column created SqlType(DATETIME)
   *  @param modified Database column modified SqlType(DATETIME) */
  case class UrlsRow(urlSrl: Long, url: String, created: java.sql.Timestamp, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching UrlsRow objects using plain SQL queries */
  implicit def GetResultUrlsRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[UrlsRow] = GR{
    prs => import prs._
    UrlsRow.tupled((<<[Long], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table urls. Objects of this class serve as prototypes for rows in queries. */
  class Urls(_tableTag: Tag) extends profile.api.Table[UrlsRow](_tableTag, Some("rocket_font_main_db"), "urls") {
    def * = (urlSrl, url, created, modified) <> (UrlsRow.tupled, UrlsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(urlSrl), Rep.Some(url), Rep.Some(created), Rep.Some(modified))).shaped.<>({r=>import r._; _1.map(_=> UrlsRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column url_srl SqlType(BIGINT), AutoInc, PrimaryKey */
    val urlSrl: Rep[Long] = column[Long]("url_srl", O.AutoInc, O.PrimaryKey)
    /** Database column url SqlType(VARCHAR), Length(255,true) */
    val url: Rep[String] = column[String]("url", O.Length(255,varying=true))
    /** Database column created SqlType(DATETIME) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column modified SqlType(DATETIME) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Uniqueness Index over (url) (database name urls_url_uindex) */
    val index1 = index("urls_url_uindex", url, unique=true)
  }
  /** Collection-like TableQuery object for table Urls */
  lazy val Urls = new TableQuery(tag => new Urls(tag))
}
