package conway.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import main.java.conway.domain.GameController;
import main.java.conway.domain.IGrid;
import main.java.conway.domain.IOutDev;
import main.java.conway.domain.Life;
import main.java.conway.domain.LifeController;
import main.java.conway.domain.LifeInterface;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;



public class IoJavalin implements IOutDev{
	
	private GameController controller;
	private LifeInterface life;
	private WsContext context;
	
	public IoJavalin() {
		life = new Life(20, 20);
		controller = new LifeController(life, this);
		
        var app = Javalin.create(config -> {
			config.staticFiles.add(staticFiles -> {
				staticFiles.directory = "/page";
				staticFiles.location = Location.CLASSPATH; // Cerca dentro il JAR/Classpath
				/*
				 * i file sono "impacchettati" con il codice, non cercati sul disco rigido esterno.
				 */
		    });
		}).start(8080);
        
        app.get("/", ctx -> {
    		Path path = Path.of("./src/main/resources/page/ConwayInOutPage.html");   
		    if (Files.exists(path)) {
		        // Usiamo Files.newInputStream che è più moderno di FileInputStream
		        ctx.contentType("text/html").result(Files.newInputStream(path));
		    } else {
		        ctx.status(404).result("File non trovato nel file system");
		    }
		    //ctx.result("Hello from Java!"));  //la forma più semplice di risposta
        }); 
        
        app.get("/greet/{name}", ctx -> {
            String name = ctx.pathParam("name");
            ctx.result("Hello, " + name + "!");
        }); //http://localhost:7070/greet/Alice
        
        app.get("/api/users", ctx -> {
            Map<String, Object> user = Map.of("id", 1, "name", "Bob");
            ctx.json(user); // Auto-converts to JSON
        });
        
        /*
         * Javalin v5+): Si passa solo la "promessa" (il Supplier del Future). 
         * Javalin è diventato più intelligente: se il Future restituisce una Stringa, 
         * lui fa ctx.result(stringa). Se restituisce un oggetto, lui fa ctx.json(oggetto).
         * 
         */
        app.get("/async", ctx -> {
        	ctx.future(() -> {
	        	// Creiamo il future
	            CompletableFuture<String> future = new CompletableFuture<>();
	            
	            // Eseguiamo il lavoro in un altro thread
	            new Thread(() -> { 
	                try {
	                    Thread.sleep(2000); // Simulazione calcolo pesante
	                    future.complete("Risultato calcolato asincronamente");
	                } catch (Exception e) {
	                    future.completeExceptionally(e);
	                }
	            });
	            
	            return future; // Restituiamo il future a Javalin
        	});
        });
        
        app.get("/async1", ctx -> {
            ctx.future(() -> CompletableFuture.supplyAsync(() -> {
                // Simuliamo l'operazione lenta
                try {
                    Thread.sleep(2000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "Risultato calcolato con supplyAsync";
            }));
        });
        
        app.ws("/chat", ws -> {
            ws.onConnect(ctx -> { 
            	System.out.println("Client connected!");
            	context = ctx;
            });
            ws.onMessage(ctx -> {
                String message = ctx.message();
                var msg = new ApplMessage(message);
                System.out.println(msg.toString());
                
                if (msg.msgContent().equals("start")) {
                	controller.onStart();
                } else if (msg.msgContent().equals("stop")) {
                	controller.onStop();
                } else if (msg.msgContent().equals("clear")) {
                	controller.onClear();
                } else if (msg.msgContent().contains("cell(")) {
                	
                	String m = msg.msgContent();
                	String numOnly = m.replace("cell(", "").replace(")", "");
                	String[] coord = numOnly.split(",");
                	if(coord.length == 2) {
                		int col = Integer.parseInt(coord[0].trim());
                		int row = Integer.parseInt(coord[1].trim());
                		
                		System.out.println("Switched cell " + row + " " + col);
                		controller.switchCellState(row, col);
                	}
                }
                
            });
        });        
	}
	
	public static void main(String[] args) {
		var resource = IoJavalin.class.getResource("/pages");
		CommUtils.outgreen("DEBUG: La cartella /page si trova in: " + resource);
		new IoJavalin();
	}

	@Override
	public void display(String msg) {
		context.send(msg);			
	}

	@Override
	public void displayCell(IGrid grid, int x, int y) {
		System.out.println("Displaycell");		
	}

	@Override
	public void close() {
		context.closeSession();
	}

	@Override
	public void displayGrid(IGrid grid) {
		if (context == null) {
			return;
		}
	
		for (int row = 0; row < grid.getRowsNum(); row++) {
			for (int column = 0; column < grid.getColsNum(); column++) {
				var cellstate = life.getCell(row, column).isAlive() ? "1" : "0";
				context.send("cell(" + row + "," + column + "," + cellstate + ")");
			}
		}
	}
}
