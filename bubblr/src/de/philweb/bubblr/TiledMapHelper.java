package de.philweb.bubblr;

/**
 *   Copyright 2011 David Kirchner dpk@dpk.net
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 * TiledMapHelper can simplify your game's tiled map operations. You can find
 * some sample code using this class at my blog:
 * 
 * http://dpk.net/2011/05/08/libgdx-box2d-tiled-maps-full-working-example-part-2/
 * 
 * Note: This code does have some limitations. It only supports single-layered
 * maps.
 * 
 * This code is based on TiledMapTest.java found at:
 * http://code.google.com/p/libgdx/
 */

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public class TiledMapHelper {
	
	private static final int[] layersList = { 0 };

//	public static final int TILEDMAPHEIGHT = 26;
//	public static final int tilesize_x = 18; 
//	public static final int tilesize_y = 18;
	
//	private OrthographicCamera camera;

	private FileHandle packFileDirectory;
	private TileAtlas tileAtlas;
	protected TileMapRenderer tileMapRenderer;
	private TiledMap map;
	
	
////------------ liest die objekte (monster + player) aus der map --------------------------------


	
	public ArrayList<GameObject> getObjectList(String ID, float pixelPerMeter) {
		
		ArrayList<GameObject> objectList = new ArrayList<GameObject>();
		
		int anz_objectGroups = getMap().objectGroups.size();	
		
		for (int counter_groups = 0; counter_groups < anz_objectGroups; counter_groups++) {
		
			if (getMap().objectGroups.get(counter_groups).name.equals(ID)) {
				
				int anz_objekte = getMap().objectGroups.get(counter_groups).objects.size();	
				
				for (int counter = 0; counter < anz_objekte; counter++) {
					
					GameObject objekt  = new GameObject();
					
					float x = getMap().objectGroups.get(counter_groups).objects.get(counter).x / pixelPerMeter;
					float y = (480 - getMap().objectGroups.get(counter_groups).objects.get(counter).y) / pixelPerMeter;
					objekt.x = x;
					objekt.y = y;
					objekt.assetName = getMap().objectGroups.get(counter_groups).objects.get(counter).name;
					objectList.add(objekt);
				}
			}
		}

		return objectList;
	}
	
	//-------------------------------------------------------
	
	
	
////------------ liest die objekte (monster + player) aus der map --------------------------------

	public ArrayList<Vector2> getCharacterList(String ID, float pixelPerMeter) {
		
		ArrayList<Vector2> objectList = new ArrayList<Vector2>();
		
//		Gdx.app.log("ID", "" + ID);
		
		int anz_objectGroups = getMap().objectGroups.size();	
		
		for (int counter_groups = 0; counter_groups < anz_objectGroups; counter_groups++) {

			if (getMap().objectGroups.get(counter_groups).name.equals(ID)) {
				
				int anz_objekte = getMap().objectGroups.get(counter_groups).objects.size();	
				
				for (int counter = 0; counter < anz_objekte; counter++) {
					
					float x = getMap().objectGroups.get(counter_groups).objects.get(counter).x / pixelPerMeter;
					float y = (480 - getMap().objectGroups.get(counter_groups).objects.get(counter).y) / pixelPerMeter;
					
					Vector2 objekt  = new Vector2();
					objekt.set(x, y);	// TODO
					objectList.add(objekt);
				}
			}
		}
		return objectList;
	}
	 
////------------------------------------------------------------------
	
	
	
	public String getMapPropertyString(String propertyName) {
		
		String ret = "";									
		
		if(map.properties.containsKey(propertyName)) {
			String parse = map.properties.get(propertyName);
			if (parse.equals("")) {
			}
			else {
				ret = parse;
			}
		}
		return ret;
	}
	
	
	
	public Integer getMapPropertyInt(String propertyName) {
		
		int ret = 0;											// TODO: liefert im zweifel immer null zur�ck... also aufpassen!
		
		if(map.properties.containsKey(propertyName)) {
			String parse = map.properties.get(propertyName);
			if (parse.equals("")) {
			}
			else {
				ret = Integer.parseInt(parse);
			}
		}
		return ret;
	}
	
	
	
	
	public Float getMapPropertyFloat(String propertyName) {
		
		float ret = 0f;											// TODO: liefert im zweifel immer null zur�ck... also aufpassen!
		
		if(map.properties.containsKey(propertyName)) {
			String parse = map.properties.get(propertyName);
			if (parse.equals("")) {
			}
			else {
				ret = Float.parseFloat(parse);
			}
		}
		return ret;
	}
	
	
	public Boolean getMapPropertyBoolean(String propertyName) {
		
		boolean ret = false;											// TODO: liefert im zweifel immer null zur�ck... also aufpassen!
		
		if(map.properties.containsKey(propertyName)) {
			String parse = map.properties.get(propertyName);
			if (parse.equals("")) {
			}
			else {
				ret = Boolean.parseBoolean(parse);
			}
		}
		return ret;
	}
	
////------------------------------------------------------------------
	
	
	
	/**
	 * Renders the part of the map that should be visible to the user.
	 */
	public void render(int x, int y, int width, int height) {
		
		tileMapRenderer.render(x, y, width, height, layersList);
	}

	/**
	 * Get the height of the map in pixels
	 * 
	 * @return y
	 */
	public int getHeight() {
		return map.height * map.tileHeight;
	}

	/**
	 * Get the width of the map in pixels
	 * 
	 * @return x
	 */
	public int getWidth() {
		return map.width * map.tileWidth;
	}

	/**
	 * Get the map, useful for iterating over the set of tiles found within
	 * 
	 * @return TiledMap
	 */
	public TiledMap getMap() {
		return map;
	}

	/**
	 * Calls dispose on all disposable resources held by this object.
	 */
	public void dispose() {
		tileAtlas.dispose();
		tileMapRenderer.dispose();
	}

	/**
	 * Sets the directory that holds the game's pack files and tile sets.
	 * 
	 * @param packDirectory
	 */
	public void setPackerDirectory(String packDirectory) {
		packFileDirectory = Gdx.files.internal(packDirectory);
	}

	/**
	 * Loads the requested tmx map file in to the helper.
	 * 
	 * @param tmxFile
	 */
	public void loadMap(String tmxFile) {
		if (packFileDirectory == null) {
			throw new IllegalStateException("loadMap() called out of sequence");
		}

		map = TiledLoader.createMap(Gdx.files.internal(tmxFile));
		tileAtlas = new TileAtlas(map, packFileDirectory);

//		tileMapRenderer = new TileMapRenderer(map, tileAtlas, 16, 16);			// wof�r stehen 16, 16 : The tilesPerBlockX and tilesPerBlockY parameters will need to be adjusted for best performance. Smaller values will cull more precisely, but result in longer loading times. Larger values result in shorter loading times, but will cull less precisely. 
		tileMapRenderer = new TileMapRenderer(map, tileAtlas, WorldRenderer.tiles_horizontally, WorldRenderer.tiles_vertically);	
	}

	/**
	 * Reads a file describing the collision boundaries that should be set
	 * per-tile and adds static bodies to the boxd world.
	 * 
	 * @param collisionsFile
	 * @param world
	 * @param pixelsPerMeter
	 *            the pixels per meter scale used for this world
	 */
	public void loadCollisions(String collisionsFile, World world,
			float pixelsPerMeter) {
		/**
		 * Detect the tiles and dynamically create a representation of the map
		 * layout, for collision detection. Each tile has its own collision
		 * rules stored in an associated file.
		 * 
		 * The file contains lines in this format (one line per type of tile):
		 * tileNumber XxY,XxY XxY,XxY
		 * 
		 * Ex:
		 * 
		 * 3 0x0,31x0 ... 4 0x0,29x0 29x0,29x31
		 * 
		 * For a 32x32 tileset, the above describes one line segment for tile #3
		 * and two for tile #4. Tile #3 has a line segment across the top. Tile
		 * #1 has a line segment across most of the top and a line segment from
		 * the top to the bottom, 30 pixels in.
		 */

		FileHandle fh = Gdx.files.internal(collisionsFile);
		String collisionFile = fh.readString();
		String lines[] = collisionFile.split("\\r?\\n");

		HashMap<Integer, ArrayList<LineSegment>> tileCollisionJoints = new HashMap<Integer, ArrayList<LineSegment>>();

		/**
		 * Some locations on the map (perhaps most locations) are "undefined",
		 * empty space, and will have the tile type 0. This code adds an empty
		 * list of line segments for this "default" tile.
		 */
		tileCollisionJoints.put(Integer.valueOf(0),
				new ArrayList<LineSegment>());

		for (int n = 0; n < lines.length; n++) {
			String cols[] = lines[n].split(" ");
			int tileNo = Integer.parseInt(cols[0]);

			ArrayList<LineSegment> tmp = new ArrayList<LineSegment>();

			for (int m = 1; m < cols.length; m++) {
				String coords[] = cols[m].split(",");

				String start[] = coords[0].split("x");
				String end[] = coords[1].split("x");

				tmp.add(new LineSegment(Integer.parseInt(start[0]), Integer
						.parseInt(start[1]), Integer.parseInt(end[0]), Integer
						.parseInt(end[1])));
			}

			tileCollisionJoints.put(Integer.valueOf(tileNo), tmp);
		}

		ArrayList<LineSegment> collisionLineSegments = new ArrayList<LineSegment>();

		for (int y = 0; y < getMap().height; y++) {
			for (int x = 0; x < getMap().width; x++) {
				int tileType = getMap().layers.get(0).tiles[(getMap().height - 1)
						- y][x];

				for (int n = 0; n < tileCollisionJoints.get(
						Integer.valueOf(tileType)).size(); n++) {
					LineSegment lineSeg = tileCollisionJoints.get(
							Integer.valueOf(tileType)).get(n);

					addOrExtendCollisionLineSegment(x * getMap().tileWidth
							+ lineSeg.start().x, y * getMap().tileHeight
							- lineSeg.start().y + getMap().tileHeight, x
							* getMap().tileWidth + lineSeg.end().x, y
							* getMap().tileHeight - lineSeg.end().y
							+ getMap().tileHeight, collisionLineSegments);
					}
			}
		}

		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyDef.BodyType.StaticBody;
		Body groundBody = world.createBody(groundBodyDef);
		
		
		for (LineSegment lineSegment : collisionLineSegments) {
						
			EdgeShape environmentShape = new EdgeShape();
			
			environmentShape.set(
					lineSegment.start().mul(1 / pixelsPerMeter), lineSegment.end().mul(1 / pixelsPerMeter));

			groundBody.createFixture(environmentShape, 0);
            
			environmentShape.dispose();
			
		}
		
		

		//--- f�r kollisionserkennung im contactlistener --------------------------------------------------------
		
		ArrayList<Fixture> groundFixtures = new ArrayList<Fixture>();
		groundFixtures = groundBody.getFixtureList();
		for (int i = 0; i < groundFixtures.size(); i++) {		
			groundFixtures.get(i).setUserData(Welt.groundFixtureString);	
			
		}	//-------------------------------------------------------------------------------------------------------
		
		
		
		/**
		 * Drawing a boundary around the entire map. We can't use a box because
		 * then the world objects would be inside and the physics engine would
		 * try to push them out.
		 */

		EdgeShape mapBounds = new EdgeShape();
		mapBounds.set(new Vector2(0.0f, 0.0f), new Vector2(getWidth()
				/ pixelsPerMeter, 0.0f));
		

		
		//---- mapbounds abgeschaltet damit player unten raus und oben reinfallen kann
//		groundBody.createFixture(mapBounds, 0);
//
//		mapBounds.set(new Vector2(0.0f, getHeight() / pixelsPerMeter),
//				new Vector2(getWidth() / pixelsPerMeter, getHeight()
//						/ pixelsPerMeter));
//		groundBody.createFixture(mapBounds, 0);
//
//		mapBounds.set(new Vector2(0.0f, 0.0f), new Vector2(0.0f,
//				getHeight() / pixelsPerMeter));
//		groundBody.createFixture(mapBounds, 0);
//
//		mapBounds.set(new Vector2(getWidth() / pixelsPerMeter, 0.0f),
//				new Vector2(getWidth() / pixelsPerMeter, getHeight()
//						/ pixelsPerMeter));
//		groundBody.createFixture(mapBounds, 0);

		mapBounds.dispose();
	}

	/**
	 * This is a helper function that makes calls that will attempt to extend
	 * one of the line segments already tracked by TiledMapHelper, if possible.
	 * The goal is to have as few line segments as possible.
	 * 
	 * Ex: If you have a line segment in the system that is from 1x1 to 3x3 and
	 * this function is called for a line that is 4x4 to 9x9, rather than add a
	 * whole new line segment to the list, the 1x1,3x3 line will be extended to
	 * 1x1,9x9. See also: LineSegment.extendIfPossible.
	 * 
	 * @param lsx1
	 *            starting x of the new line segment
	 * @param lsy1
	 *            starting y of the new line segment
	 * @param lsx2
	 *            ending x of the new line segment
	 * @param lsy2
	 *            ending y of the new line segment
	 * @param collisionLineSegments
	 *            the current list of line segments
	 */
	private void addOrExtendCollisionLineSegment(float lsx1, float lsy1,
			float lsx2, float lsy2, ArrayList<LineSegment> collisionLineSegments) {
		LineSegment line = new LineSegment(lsx1, lsy1, lsx2, lsy2);

		boolean didextend = false;

		for (LineSegment test : collisionLineSegments) {
			if (test.extendIfPossible(line)) {
				didextend = true;
				break;
			}
		}

		if (!didextend) {
			collisionLineSegments.add(line);
		}
	}


	/**
	 * Describes the start and end points of a line segment and contains a
	 * helper method useful for extending line segments.
	 */
	private class LineSegment {
		private Vector2 start = new Vector2();
		private Vector2 end = new Vector2();

		/**
		 * Construct a new LineSegment with the specified coordinates.
		 * 
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 */
		public LineSegment(float x1, float y1, float x2, float y2) {
			start = new Vector2(x1, y1);
			end = new Vector2(x2, y2);
		}

		/**
		 * The "start" of the line. Start and end are misnomers, this is just
		 * one end of the line.
		 * 
		 * @return Vector2
		 */
		public Vector2 start() {
			return start;
		}

		/**
		 * The "end" of the line. Start and end are misnomers, this is just one
		 * end of the line.
		 * 
		 * @return Vector2
		 */
		public Vector2 end() {
			return end;
		}

		/**
		 * Determine if the requested line could be tacked on to the end of this
		 * line with no kinks or gaps. If it can, the current LineSegment will
		 * be extended by the length of the passed LineSegment.
		 * 
		 * @param lineSegment
		 * @return boolean true if line was extended, false if not.
		 */
		public boolean extendIfPossible(LineSegment lineSegment) {
			/**
			 * First, let's see if the slopes of the two segments are the same.
			 */
			double slope1 = Math.atan2(end.y - start.y, end.x - start.x);
			double slope2 = Math.atan2(lineSegment.end.y - lineSegment.start.y,
					lineSegment.end.x - lineSegment.start.x);

			if (Math.abs(slope1 - slope2) > 1e-9) {
				return false;
			}

			/**
			 * Second, check if either end of this line segment is adjacent to
			 * the requested line segment. So, 1 pixel away up through sqrt(2)
			 * away.
			 * 
			 * Whichever two points are within the right range will be "merged"
			 * so that the two outer points will describe the line segment.
			 */
			if (start.dst(lineSegment.start) <= Math.sqrt(2) + 1e-9) {
				start.set(lineSegment.end);
				return true;
			} else if (end.dst(lineSegment.start) <= Math.sqrt(2) + 1e-9) {
				end.set(lineSegment.end);
				return true;
			} else if (end.dst(lineSegment.end) <= Math.sqrt(2) + 1e-9) {
				end.set(lineSegment.start);
				return true;
			} else if (start.dst(lineSegment.end) <= Math.sqrt(2) + 1e-9) {
				start.set(lineSegment.start);
				return true;
			}

			return false;
		}

		/**
		 * Returns a pretty description of the LineSegment.
		 * 
		 * @return String
		 */
		@Override
		public String toString() {
			return "[" + start.x + "x" + start.y + "] -> [" + end.x + "x"
					+ end.y + "]";
		}
	}


}


