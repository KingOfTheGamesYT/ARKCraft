package com.tom.blockstategenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import com.uberverse.arkcraft.common.block.BlockCropPlot.BerryColor;
import com.uberverse.arkcraft.common.block.tile.TileEntityCropPlotNew.CropPlotType;

public class BlockStateGenerator {
	private static final String BERRY_TEXTURE_PREFIX = "arkcraft:blocks/berry_leaves_!";
	private static final int MAX_AGE = 5;

	public static void main(String[] args) {
		System.out.println("[BSG]: Starting Block State Generator...");
		try{
			File f = new File(".", "crop_plot.json");
			System.out.println("[BSG]: Output: "+f.getAbsolutePath());
			PrintWriter out = new PrintWriter(new FileWriter(f));
			out.println("{");
			out.println("  \"_comment\": \"This blockstate file was created by Tom's Block State Generator. com.tom.blockstategenerator\",");
			out.println("  \"forge_marker\": 1,");
			System.out.println("[BSG]: Writing Defaults");
			out.println("  \"defaults\": {");
			out.println("    \"textures\": { \"berry\": \""+BERRY_TEXTURE_PREFIX.replace("!", BerryColor.VALUES[0].getName())+"\" }");
			out.println("  },");
			out.println("  \"variants\": {");//age=1,berry=narco,type=medium
			/*System.out.println("[BSG]: Writing Base Models");
			out.println("    \"type\":{"); //Moved
			out.println("      \"small\":{ \"model\": \"arkcraft:crop_plot_small\" },");
			out.println("      \"medium\":{ \"model\": \"arkcraft:crop_plot_medium\" },");
			out.println("      \"large\":{ \"model\": \"arkcraft:crop_plot_large\" }");
			out.println("    },");*/
			for(int i = 0;i<MAX_AGE;i++){
				for(BerryColor c : BerryColor.VALUES){
					for(CropPlotType t : CropPlotType.VALUES){
						boolean notWriteComma = i == MAX_AGE - 1;
						if(notWriteComma){
							notWriteComma = notWriteComma && c.ordinal() == BerryColor.VALUES.length - 1 && t.ordinal() == CropPlotType.VALUES.length - 1;
						}
						String head = "age="+i+",berry="+c.getName()+",type="+t.getName();
						String model = "arkcraft:crop_plot_"+t.getName()+"_"+(i-1);
						String texture = BERRY_TEXTURE_PREFIX.replace("!", c.getName());
						if(i == 0){
							out.println("    \""+head+"\": { \"model\": \"arkcraft:crop_plot_"+t.getName()+"\"}" + (notWriteComma ? "" : ","));
						}else
							out.println("    \""+head+"\": { \"model\": \"arkcraft:crop_plot_"+t.getName()+"\", \"submodel\": {\"plant\": {\"model\": \""+model+"\"}}, \"textures\": {\"berry\": \""+texture+"\""+"} }" + (notWriteComma ? "" : ","));
						System.out.println("[BSG]: Writing: "+head+" Resource names: Model: "+model+" Texture: "+texture);
					}
				}
			}
			out.println("  }");
			out.println("}");
			out.close();
			System.out.println("[BSG]: Block State Generator Finished! Output: "+f.getAbsolutePath());
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("[BSG]: Done");
	}
	/**{
    "forge_marker": 1,
    "defaults": {
        "textures": {
            "berry": "blocks/planks_oak"
        }
    },
    "variants": {
        "type":{
            "small":{
                "model": "arkcraft:crop_plot_small"
            },
            "medium":{
                "model": "arkcraft:crop_plot_medium"
            },
            "large":{
                "model": "arkcraft:crop_plot_large"
            }
        },
        "age":{
            0:{},
            1:{
//                "type":{
//                    "small":{
                        "submodel": "arkcraft:plant1"
//                    },
//                    "medium":{
//                        "submodel": "arkcraft:plant1"
//                    },
//                    "large":{
//                        "submodel": "arkcraft:largePlant1"
//                    }
//                }
            },
            2:{
//                "type":{
//                    "small":{
                        "submodel": "arkcraft:plant2"
//                    },
//                    "medium":{
//                        "submodel": "arkcraft:plant2"
//                    },
//                    "large":{
//                        "submodel": "arkcraft:largePlant2"
//                    }
//                }
            },
            3:{
//                "type":{
//                    "small":{
                        "submodel": "arkcraft:plant3"
//                    },
//                    "medium":{
//                        "submodel": "arkcraft:plant3"
//                    },
//                    "large":{
//                        "submodel": "arkcraft:largePlant3"
//                    }
//                }
            },
            4:{
//                "type":{
//                    "small":{
                        "submodel": "arkcraft:plant4"
//                    },
//                    "medium":{
//                        "submodel": "arkcraft:plant4"
//                    },
//                    "large":{
//                        "submodel": "arkcraft:largePlant4"
//                    }
//                }
            }
        },
        "berry":{
            "amar":{
                "textures":{
                    "berry": "arkcraft:blocks/berry_leaves_amar"
                }
            },
            "azul":{
                "textures":{
                    "berry": "arkcraft:blocks/berry_leaves_azul"
                }
            },
            "mejo":{
                "textures":{
                    "berry": "arkcraft:blocks/berry_leaves_mejo"
                }
            },
            "narco":{
                "textures":{
                    "berry": "arkcraft:blocks/berry_leaves_narco"
                }
            },
            "stim":{
                "textures":{
                    "berry": "arkcraft:blocks/berry_leaves_stim"
                }
            },
            "tinto":{
                "textures":{
                    "berry": "arkcraft:blocks/berry_leaves_tinto"
                }
            }
        }
    }
}*/
}
