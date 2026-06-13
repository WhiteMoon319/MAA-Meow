cmake_minimum_required(VERSION 3.22)

project("maameow_shizuku")

set(CMAKE_CXX_STANDARD 17)

add_compile_options(-Werror=format -fdata-sections -ffunction-sections -fno-exceptions -fno-rtti -fno-threadsafe-statics)

if (NOT CMAKE_BUILD_TYPE STREQUAL "Debug")
    add_compile_options(-Os -flto -fvisibility=hidden -fvisibility-inlines-hidden)
    add_link_options(-flto -Wl,--exclude-libs,ALL -Wl,--gc-sections -Wl,--strip-all)
else ()
    add_definitions(-DDEBUG)
endif ()

find_package(boringssl REQUIRED CONFIG)
find_package(cxx REQUIRED CONFIG)

add_library(adb_pairing SHARED
        adb_pairing.cpp misc.cpp)

target_link_libraries(adb_pairing log boringssl::crypto_static cxx::cxx)

if (NOT CMAKE_BUILD_TYPE STREQUAL "Debug")
    add_custom_command(TARGET adb_pairing POST_BUILD
            COMMAND ${CMAKE_STRIP} --remove-section=.comment "${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/libadb_pairing.so")
endif ()
