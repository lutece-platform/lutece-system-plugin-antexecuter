package fr.paris.lutece.plugins.antexecuter.business;

import fr.paris.lutece.util.sql.DAOUtil;

public class CheckDatabaseDAO {

	 private static final String SQL_QUERY_SELECTALL = "SELECT * FROM core_admin_right";
	 
	 
	 public static void checkDataBase( )
	 {
	    try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL ) )
	     {
	         daoUtil.executeQuery( );
	     }
	 }
}
