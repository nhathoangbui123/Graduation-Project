set(COMPONENT_SRCS
	"NVSDriver.c"
	"wifi.c"
	"aws.c"
	"pzem.cpp"
	"nextion.c"
	"ws2812_driver.c"
	"cmd.c"
	"app_main.cpp"
	)

set(COMPONENT_ADD_INCLUDEDIRS
	"."
	"${CMAKE_CURRENT_LIST_DIR}"
	)

idf_component_register(SRCS "${COMPONENT_SRCS}"
					   INCLUDE_DIRS ${COMPONENT_ADD_INCLUDEDIRS}
					  )