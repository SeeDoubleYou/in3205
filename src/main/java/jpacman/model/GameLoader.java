package jpacman.model;


/**
 * Take care of loading a map of a game from a
 * resource file.
 * 
 * @author Arie van Deursen, Delft University of Technology, January 2009
 * @version $Id: GameLoader.java,v 1.16 2009/02/03 08:34:01 arie Exp $
 */

public class GameLoader {
    
    /**
     * The default map of the world, used if
     * no specific map file is provided, or if loading the
     * map file is going wrong.
     */
    static final String[] DEFAULT_WORLD_MAP = new String[] {
        "WWWWWWWWWWWWWWWWWWWW",
        "W000M0W00000M0000F0W",
        "W0F00MW000000000000W",
        "W0000000000M000F000W",
        "W000000000000F00000W",
        "W000MM0000M00000000W",
        "W000000M00000000000W",
        "W00M00000F0000000WWW",
        "W0000000000000WWW00W",
        "W000000000000000000W",
        "W0000WW0WWW00000000W",
        "W0000W00F0W00000000W",
        "W0P00W0M00W00000000W",
        "W0000WWWWWW00000F00W",
        "W000000000000000000W",
        "W000000000000000000W",
        "W0F0000000000F00M00W",
        "W0WWWW0000000000000W",
        "W00F00WWWW000000WWWW",
        "WWWWWWWWWWWWWWWWWWWW"
    };

    /**
     * Actually obtain a map from a file specified in the 
     * default property file.
     * @return The map in the file
     * @throws GameLoadException If the map is wrong or the files can't be opened.
     */
    public String[] obtainMap() throws GameLoadException {
         // TODO. Currently always return default map.
         return DEFAULT_WORLD_MAP; 
        
    }
    


    /**
     * Check the correctness of a given map.
     * @param map The map to be checked.
     * @return null if the map is ok, an error message explaining the cause otherwise.
     */
    public static String checkSanity(String[] map) {
        assert map != null;
        
        int height = map.length;
        if (height == 0) {
            return "Empty board not allowed";
        }
        int width = map[0].length();
        if (width == 0) {
            return "Empty rows not permitted.";
        }
        
       boolean playerEncountered = false;
        
        for (int y = 0; y < height; y++) {
            if (map[y].length() != width) {
                return "all lines in map should be of equal length.";
            }
            for (int x = 0; x < width; x++) {
                char ch = map[y].charAt(x);
                if (!permittedChar(ch)) {
                    return "Incorrect game character: " + ch;
                }
                if (ch == Guest.PLAYER_TYPE) {
                    playerEncountered = true;
                }
            }
       }
        if (!playerEncountered) {
            return "No player defined.";
        }
        
        return null; // = ok.
    }
    
    /**
     * Identify whether a character is permitted on a map.
     * @param c The char to be checked
     * @return true iff c is a valid char.
     */
    private static boolean permittedChar(char c) {
        switch(c) {
        case Guest.EMPTY_TYPE:
        case Guest.FOOD_TYPE:
        case Guest.MONSTER_TYPE:
        case Guest.PLAYER_TYPE:
        case Guest.WALL_TYPE:
            return true;
        default:
            return false;
        }     
    }    
}