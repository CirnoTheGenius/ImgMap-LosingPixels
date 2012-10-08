package cirno;

import net.minecraft.server.WorldMap;
import net.minecraft.server.WorldMapDecoration;

import org.bukkit.craftbukkit.map.CraftMapView;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class RegularRenderer extends MapRenderer {

	private final WorldMap worldMap;
	
	public RegularRenderer(CraftMapView mapView, WorldMap worldMap) {
		super(false);
		this.worldMap = worldMap;
	}

	public void render(MapView map, MapCanvas canvas, Player player) {
		for (int x = 0; x < 128; x++) {
			for (int y = 0; y < 128; y++)
				canvas.setPixel(x, y, worldMap.colors[y * 128 + x]);

		}

		MapCursorCollection cursors;
		for (cursors = canvas.getCursors(); cursors.size() > 0; cursors
				.removeCursor(cursors.getCursor(0)));
		for (int i = 0; i < worldMap.decorations.size(); i++) {
			WorldMapDecoration decoration = (WorldMapDecoration) worldMap.decorations
					.get(i);
			cursors.addCursor(decoration.locX, decoration.locY,
					(byte) (decoration.rotation & 15), decoration.type);
		}

	}

}
