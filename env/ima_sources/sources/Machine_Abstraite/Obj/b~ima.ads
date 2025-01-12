pragma Warnings (Off);
pragma Ada_95;
with System;
with System.Parameters;
with System.Secondary_Stack;
package ada_main is

   gnat_argc : Integer;
   gnat_argv : System.Address;
   gnat_envp : System.Address;

   pragma Import (C, gnat_argc);
   pragma Import (C, gnat_argv);
   pragma Import (C, gnat_envp);

   gnat_exit_status : Integer;
   pragma Import (C, gnat_exit_status);

   GNAT_Version : constant String :=
                    "GNAT Version: 9.5.0" & ASCII.NUL;
   pragma Export (C, GNAT_Version, "__gnat_version");

   Ada_Main_Program_Name : constant String := "_ada_ima" & ASCII.NUL;
   pragma Export (C, Ada_Main_Program_Name, "__gnat_ada_main_program_name");

   procedure adainit;
   pragma Export (C, adainit, "adainit");

   procedure adafinal;
   pragma Export (C, adafinal, "adafinal");

   function main
     (argc : Integer;
      argv : System.Address;
      envp : System.Address)
      return Integer;
   pragma Export (C, main, "main");

   type Version_32 is mod 2 ** 32;
   u00001 : constant Version_32 := 16#7ee8ce1c#;
   pragma Export (C, u00001, "imaB");
   u00002 : constant Version_32 := 16#050ff2f0#;
   pragma Export (C, u00002, "system__standard_libraryB");
   u00003 : constant Version_32 := 16#4113f22b#;
   pragma Export (C, u00003, "system__standard_libraryS");
   u00004 : constant Version_32 := 16#76789da1#;
   pragma Export (C, u00004, "adaS");
   u00005 : constant Version_32 := 16#4fc9bc76#;
   pragma Export (C, u00005, "ada__command_lineB");
   u00006 : constant Version_32 := 16#3cdef8c9#;
   pragma Export (C, u00006, "ada__command_lineS");
   u00007 : constant Version_32 := 16#4635ec04#;
   pragma Export (C, u00007, "systemS");
   u00008 : constant Version_32 := 16#f32b4133#;
   pragma Export (C, u00008, "system__secondary_stackB");
   u00009 : constant Version_32 := 16#03a1141d#;
   pragma Export (C, u00009, "system__secondary_stackS");
   u00010 : constant Version_32 := 16#9762c9f2#;
   pragma Export (C, u00010, "ada__exceptionsB");
   u00011 : constant Version_32 := 16#585ef86b#;
   pragma Export (C, u00011, "ada__exceptionsS");
   u00012 : constant Version_32 := 16#5726abed#;
   pragma Export (C, u00012, "ada__exceptions__last_chance_handlerB");
   u00013 : constant Version_32 := 16#41e5552e#;
   pragma Export (C, u00013, "ada__exceptions__last_chance_handlerS");
   u00014 : constant Version_32 := 16#ae860117#;
   pragma Export (C, u00014, "system__soft_linksB");
   u00015 : constant Version_32 := 16#0336e7b2#;
   pragma Export (C, u00015, "system__soft_linksS");
   u00016 : constant Version_32 := 16#75bf515c#;
   pragma Export (C, u00016, "system__soft_links__initializeB");
   u00017 : constant Version_32 := 16#5697fc2b#;
   pragma Export (C, u00017, "system__soft_links__initializeS");
   u00018 : constant Version_32 := 16#86dbf443#;
   pragma Export (C, u00018, "system__parametersB");
   u00019 : constant Version_32 := 16#0ed9b82f#;
   pragma Export (C, u00019, "system__parametersS");
   u00020 : constant Version_32 := 16#41837d1e#;
   pragma Export (C, u00020, "system__stack_checkingB");
   u00021 : constant Version_32 := 16#c88a87ec#;
   pragma Export (C, u00021, "system__stack_checkingS");
   u00022 : constant Version_32 := 16#ced09590#;
   pragma Export (C, u00022, "system__storage_elementsB");
   u00023 : constant Version_32 := 16#6bf6a600#;
   pragma Export (C, u00023, "system__storage_elementsS");
   u00024 : constant Version_32 := 16#34742901#;
   pragma Export (C, u00024, "system__exception_tableB");
   u00025 : constant Version_32 := 16#1b9b8546#;
   pragma Export (C, u00025, "system__exception_tableS");
   u00026 : constant Version_32 := 16#ce4af020#;
   pragma Export (C, u00026, "system__exceptionsB");
   u00027 : constant Version_32 := 16#2e5681f2#;
   pragma Export (C, u00027, "system__exceptionsS");
   u00028 : constant Version_32 := 16#69416224#;
   pragma Export (C, u00028, "system__exceptions__machineB");
   u00029 : constant Version_32 := 16#d27d9682#;
   pragma Export (C, u00029, "system__exceptions__machineS");
   u00030 : constant Version_32 := 16#aa0563fc#;
   pragma Export (C, u00030, "system__exceptions_debugB");
   u00031 : constant Version_32 := 16#38bf15c0#;
   pragma Export (C, u00031, "system__exceptions_debugS");
   u00032 : constant Version_32 := 16#6c2f8802#;
   pragma Export (C, u00032, "system__img_intB");
   u00033 : constant Version_32 := 16#44ee0cc6#;
   pragma Export (C, u00033, "system__img_intS");
   u00034 : constant Version_32 := 16#39df8c17#;
   pragma Export (C, u00034, "system__tracebackB");
   u00035 : constant Version_32 := 16#181732c0#;
   pragma Export (C, u00035, "system__tracebackS");
   u00036 : constant Version_32 := 16#9ed49525#;
   pragma Export (C, u00036, "system__traceback_entriesB");
   u00037 : constant Version_32 := 16#466e1a74#;
   pragma Export (C, u00037, "system__traceback_entriesS");
   u00038 : constant Version_32 := 16#448e9548#;
   pragma Export (C, u00038, "system__traceback__symbolicB");
   u00039 : constant Version_32 := 16#c84061d1#;
   pragma Export (C, u00039, "system__traceback__symbolicS");
   u00040 : constant Version_32 := 16#179d7d28#;
   pragma Export (C, u00040, "ada__containersS");
   u00041 : constant Version_32 := 16#701f9d88#;
   pragma Export (C, u00041, "ada__exceptions__tracebackB");
   u00042 : constant Version_32 := 16#20245e75#;
   pragma Export (C, u00042, "ada__exceptions__tracebackS");
   u00043 : constant Version_32 := 16#5ab55268#;
   pragma Export (C, u00043, "interfacesS");
   u00044 : constant Version_32 := 16#769e25e6#;
   pragma Export (C, u00044, "interfaces__cB");
   u00045 : constant Version_32 := 16#467817d8#;
   pragma Export (C, u00045, "interfaces__cS");
   u00046 : constant Version_32 := 16#e865e681#;
   pragma Export (C, u00046, "system__bounded_stringsB");
   u00047 : constant Version_32 := 16#31c8cd1d#;
   pragma Export (C, u00047, "system__bounded_stringsS");
   u00048 : constant Version_32 := 16#0062635e#;
   pragma Export (C, u00048, "system__crtlS");
   u00049 : constant Version_32 := 16#bba79bcb#;
   pragma Export (C, u00049, "system__dwarf_linesB");
   u00050 : constant Version_32 := 16#9a78d181#;
   pragma Export (C, u00050, "system__dwarf_linesS");
   u00051 : constant Version_32 := 16#5b4659fa#;
   pragma Export (C, u00051, "ada__charactersS");
   u00052 : constant Version_32 := 16#8f637df8#;
   pragma Export (C, u00052, "ada__characters__handlingB");
   u00053 : constant Version_32 := 16#3b3f6154#;
   pragma Export (C, u00053, "ada__characters__handlingS");
   u00054 : constant Version_32 := 16#4b7bb96a#;
   pragma Export (C, u00054, "ada__characters__latin_1S");
   u00055 : constant Version_32 := 16#e6d4fa36#;
   pragma Export (C, u00055, "ada__stringsS");
   u00056 : constant Version_32 := 16#96df1a3f#;
   pragma Export (C, u00056, "ada__strings__mapsB");
   u00057 : constant Version_32 := 16#1e526bec#;
   pragma Export (C, u00057, "ada__strings__mapsS");
   u00058 : constant Version_32 := 16#d68fb8f1#;
   pragma Export (C, u00058, "system__bit_opsB");
   u00059 : constant Version_32 := 16#0765e3a3#;
   pragma Export (C, u00059, "system__bit_opsS");
   u00060 : constant Version_32 := 16#72b39087#;
   pragma Export (C, u00060, "system__unsigned_typesS");
   u00061 : constant Version_32 := 16#92f05f13#;
   pragma Export (C, u00061, "ada__strings__maps__constantsS");
   u00062 : constant Version_32 := 16#a0d3d22b#;
   pragma Export (C, u00062, "system__address_imageB");
   u00063 : constant Version_32 := 16#e7d9713e#;
   pragma Export (C, u00063, "system__address_imageS");
   u00064 : constant Version_32 := 16#ec78c2bf#;
   pragma Export (C, u00064, "system__img_unsB");
   u00065 : constant Version_32 := 16#ed47ac70#;
   pragma Export (C, u00065, "system__img_unsS");
   u00066 : constant Version_32 := 16#d7aac20c#;
   pragma Export (C, u00066, "system__ioB");
   u00067 : constant Version_32 := 16#d8771b4b#;
   pragma Export (C, u00067, "system__ioS");
   u00068 : constant Version_32 := 16#f790d1ef#;
   pragma Export (C, u00068, "system__mmapB");
   u00069 : constant Version_32 := 16#7c445363#;
   pragma Export (C, u00069, "system__mmapS");
   u00070 : constant Version_32 := 16#92d882c5#;
   pragma Export (C, u00070, "ada__io_exceptionsS");
   u00071 : constant Version_32 := 16#0cdaa54a#;
   pragma Export (C, u00071, "system__mmap__os_interfaceB");
   u00072 : constant Version_32 := 16#82f29877#;
   pragma Export (C, u00072, "system__mmap__os_interfaceS");
   u00073 : constant Version_32 := 16#834dfe5e#;
   pragma Export (C, u00073, "system__mmap__unixS");
   u00074 : constant Version_32 := 16#fa90b46b#;
   pragma Export (C, u00074, "system__os_libB");
   u00075 : constant Version_32 := 16#4542b55d#;
   pragma Export (C, u00075, "system__os_libS");
   u00076 : constant Version_32 := 16#ec4d5631#;
   pragma Export (C, u00076, "system__case_utilB");
   u00077 : constant Version_32 := 16#79e05a50#;
   pragma Export (C, u00077, "system__case_utilS");
   u00078 : constant Version_32 := 16#2a8e89ad#;
   pragma Export (C, u00078, "system__stringsB");
   u00079 : constant Version_32 := 16#2623c091#;
   pragma Export (C, u00079, "system__stringsS");
   u00080 : constant Version_32 := 16#5a3f5337#;
   pragma Export (C, u00080, "system__object_readerB");
   u00081 : constant Version_32 := 16#82413105#;
   pragma Export (C, u00081, "system__object_readerS");
   u00082 : constant Version_32 := 16#1a74a354#;
   pragma Export (C, u00082, "system__val_lliB");
   u00083 : constant Version_32 := 16#dc110aa4#;
   pragma Export (C, u00083, "system__val_lliS");
   u00084 : constant Version_32 := 16#afdbf393#;
   pragma Export (C, u00084, "system__val_lluB");
   u00085 : constant Version_32 := 16#0841c7f5#;
   pragma Export (C, u00085, "system__val_lluS");
   u00086 : constant Version_32 := 16#269742a9#;
   pragma Export (C, u00086, "system__val_utilB");
   u00087 : constant Version_32 := 16#ea955afa#;
   pragma Export (C, u00087, "system__val_utilS");
   u00088 : constant Version_32 := 16#d7bf3f29#;
   pragma Export (C, u00088, "system__exception_tracesB");
   u00089 : constant Version_32 := 16#62eacc9e#;
   pragma Export (C, u00089, "system__exception_tracesS");
   u00090 : constant Version_32 := 16#8c33a517#;
   pragma Export (C, u00090, "system__wch_conB");
   u00091 : constant Version_32 := 16#5d48ced6#;
   pragma Export (C, u00091, "system__wch_conS");
   u00092 : constant Version_32 := 16#9721e840#;
   pragma Export (C, u00092, "system__wch_stwB");
   u00093 : constant Version_32 := 16#7059e2d7#;
   pragma Export (C, u00093, "system__wch_stwS");
   u00094 : constant Version_32 := 16#a831679c#;
   pragma Export (C, u00094, "system__wch_cnvB");
   u00095 : constant Version_32 := 16#52ff7425#;
   pragma Export (C, u00095, "system__wch_cnvS");
   u00096 : constant Version_32 := 16#ece6fdb6#;
   pragma Export (C, u00096, "system__wch_jisB");
   u00097 : constant Version_32 := 16#d28f6d04#;
   pragma Export (C, u00097, "system__wch_jisS");
   u00098 : constant Version_32 := 16#f64b89a4#;
   pragma Export (C, u00098, "ada__integer_text_ioB");
   u00099 : constant Version_32 := 16#082ea75f#;
   pragma Export (C, u00099, "ada__integer_text_ioS");
   u00100 : constant Version_32 := 16#927a893f#;
   pragma Export (C, u00100, "ada__text_ioB");
   u00101 : constant Version_32 := 16#5194351e#;
   pragma Export (C, u00101, "ada__text_ioS");
   u00102 : constant Version_32 := 16#10558b11#;
   pragma Export (C, u00102, "ada__streamsB");
   u00103 : constant Version_32 := 16#67e31212#;
   pragma Export (C, u00103, "ada__streamsS");
   u00104 : constant Version_32 := 16#d398a95f#;
   pragma Export (C, u00104, "ada__tagsB");
   u00105 : constant Version_32 := 16#12a0afb8#;
   pragma Export (C, u00105, "ada__tagsS");
   u00106 : constant Version_32 := 16#796f31f1#;
   pragma Export (C, u00106, "system__htableB");
   u00107 : constant Version_32 := 16#c2f75fee#;
   pragma Export (C, u00107, "system__htableS");
   u00108 : constant Version_32 := 16#089f5cd0#;
   pragma Export (C, u00108, "system__string_hashB");
   u00109 : constant Version_32 := 16#60a93490#;
   pragma Export (C, u00109, "system__string_hashS");
   u00110 : constant Version_32 := 16#73d2d764#;
   pragma Export (C, u00110, "interfaces__c_streamsB");
   u00111 : constant Version_32 := 16#b1330297#;
   pragma Export (C, u00111, "interfaces__c_streamsS");
   u00112 : constant Version_32 := 16#71ac0ba7#;
   pragma Export (C, u00112, "system__file_ioB");
   u00113 : constant Version_32 := 16#e1440d61#;
   pragma Export (C, u00113, "system__file_ioS");
   u00114 : constant Version_32 := 16#86c56e5a#;
   pragma Export (C, u00114, "ada__finalizationS");
   u00115 : constant Version_32 := 16#95817ed8#;
   pragma Export (C, u00115, "system__finalization_rootB");
   u00116 : constant Version_32 := 16#09c79f94#;
   pragma Export (C, u00116, "system__finalization_rootS");
   u00117 : constant Version_32 := 16#bbaa76ac#;
   pragma Export (C, u00117, "system__file_control_blockS");
   u00118 : constant Version_32 := 16#f6fdca1c#;
   pragma Export (C, u00118, "ada__text_io__integer_auxB");
   u00119 : constant Version_32 := 16#09097bbe#;
   pragma Export (C, u00119, "ada__text_io__integer_auxS");
   u00120 : constant Version_32 := 16#181dc502#;
   pragma Export (C, u00120, "ada__text_io__generic_auxB");
   u00121 : constant Version_32 := 16#16b3615d#;
   pragma Export (C, u00121, "ada__text_io__generic_auxS");
   u00122 : constant Version_32 := 16#b10ba0c7#;
   pragma Export (C, u00122, "system__img_biuB");
   u00123 : constant Version_32 := 16#b49118ca#;
   pragma Export (C, u00123, "system__img_biuS");
   u00124 : constant Version_32 := 16#4e06ab0c#;
   pragma Export (C, u00124, "system__img_llbB");
   u00125 : constant Version_32 := 16#f5560834#;
   pragma Export (C, u00125, "system__img_llbS");
   u00126 : constant Version_32 := 16#9dca6636#;
   pragma Export (C, u00126, "system__img_lliB");
   u00127 : constant Version_32 := 16#577ab9d5#;
   pragma Export (C, u00127, "system__img_lliS");
   u00128 : constant Version_32 := 16#a756d097#;
   pragma Export (C, u00128, "system__img_llwB");
   u00129 : constant Version_32 := 16#5c3a2ba2#;
   pragma Export (C, u00129, "system__img_llwS");
   u00130 : constant Version_32 := 16#eb55dfbb#;
   pragma Export (C, u00130, "system__img_wiuB");
   u00131 : constant Version_32 := 16#dad09f58#;
   pragma Export (C, u00131, "system__img_wiuS");
   u00132 : constant Version_32 := 16#d763507a#;
   pragma Export (C, u00132, "system__val_intB");
   u00133 : constant Version_32 := 16#0e90c63b#;
   pragma Export (C, u00133, "system__val_intS");
   u00134 : constant Version_32 := 16#1d9142a4#;
   pragma Export (C, u00134, "system__val_unsB");
   u00135 : constant Version_32 := 16#621b7dbc#;
   pragma Export (C, u00135, "system__val_unsS");
   u00136 : constant Version_32 := 16#30fa58ff#;
   pragma Export (C, u00136, "assembleurB");
   u00137 : constant Version_32 := 16#c366f8c7#;
   pragma Export (C, u00137, "assembleurS");
   u00138 : constant Version_32 := 16#7321b1ad#;
   pragma Export (C, u00138, "ma_detiqB");
   u00139 : constant Version_32 := 16#8441efc6#;
   pragma Export (C, u00139, "ma_detiqS");
   u00140 : constant Version_32 := 16#d31516ba#;
   pragma Export (C, u00140, "mes_tablesB");
   u00141 : constant Version_32 := 16#198b1cb4#;
   pragma Export (C, u00141, "mes_tablesS");
   u00142 : constant Version_32 := 16#f5ff2cf9#;
   pragma Export (C, u00142, "types_baseB");
   u00143 : constant Version_32 := 16#bb86ef9c#;
   pragma Export (C, u00143, "types_baseS");
   u00144 : constant Version_32 := 16#acee74ad#;
   pragma Export (C, u00144, "system__compare_array_unsigned_8B");
   u00145 : constant Version_32 := 16#ef369d89#;
   pragma Export (C, u00145, "system__compare_array_unsigned_8S");
   u00146 : constant Version_32 := 16#a8025f3c#;
   pragma Export (C, u00146, "system__address_operationsB");
   u00147 : constant Version_32 := 16#55395237#;
   pragma Export (C, u00147, "system__address_operationsS");
   u00148 : constant Version_32 := 16#fd83e873#;
   pragma Export (C, u00148, "system__concat_2B");
   u00149 : constant Version_32 := 16#44953bd4#;
   pragma Export (C, u00149, "system__concat_2S");
   u00150 : constant Version_32 := 16#d96e3c40#;
   pragma Export (C, u00150, "system__finalization_mastersB");
   u00151 : constant Version_32 := 16#1dc9d5ce#;
   pragma Export (C, u00151, "system__finalization_mastersS");
   u00152 : constant Version_32 := 16#7268f812#;
   pragma Export (C, u00152, "system__img_boolB");
   u00153 : constant Version_32 := 16#b3ec9def#;
   pragma Export (C, u00153, "system__img_boolS");
   u00154 : constant Version_32 := 16#6d4d969a#;
   pragma Export (C, u00154, "system__storage_poolsB");
   u00155 : constant Version_32 := 16#65d872a9#;
   pragma Export (C, u00155, "system__storage_poolsS");
   u00156 : constant Version_32 := 16#5a895de2#;
   pragma Export (C, u00156, "system__pool_globalB");
   u00157 : constant Version_32 := 16#7141203e#;
   pragma Export (C, u00157, "system__pool_globalS");
   u00158 : constant Version_32 := 16#2323a8af#;
   pragma Export (C, u00158, "system__memoryB");
   u00159 : constant Version_32 := 16#1f488a30#;
   pragma Export (C, u00159, "system__memoryS");
   u00160 : constant Version_32 := 16#50f03bb8#;
   pragma Export (C, u00160, "pseudo_codeB");
   u00161 : constant Version_32 := 16#025396c5#;
   pragma Export (C, u00161, "pseudo_codeS");
   u00162 : constant Version_32 := 16#1384aef2#;
   pragma Export (C, u00162, "entier_esB");
   u00163 : constant Version_32 := 16#201ff234#;
   pragma Export (C, u00163, "entier_esS");
   u00164 : constant Version_32 := 16#e04da326#;
   pragma Export (C, u00164, "flottant_esB");
   u00165 : constant Version_32 := 16#d3d6ffe0#;
   pragma Export (C, u00165, "flottant_esS");
   u00166 : constant Version_32 := 16#d5f9759f#;
   pragma Export (C, u00166, "ada__text_io__float_auxB");
   u00167 : constant Version_32 := 16#48248c7b#;
   pragma Export (C, u00167, "ada__text_io__float_auxS");
   u00168 : constant Version_32 := 16#8aa4f090#;
   pragma Export (C, u00168, "system__img_realB");
   u00169 : constant Version_32 := 16#819dbde6#;
   pragma Export (C, u00169, "system__img_realS");
   u00170 : constant Version_32 := 16#42a257f7#;
   pragma Export (C, u00170, "system__fat_llfS");
   u00171 : constant Version_32 := 16#1b28662b#;
   pragma Export (C, u00171, "system__float_controlB");
   u00172 : constant Version_32 := 16#a6c9af38#;
   pragma Export (C, u00172, "system__float_controlS");
   u00173 : constant Version_32 := 16#3e932977#;
   pragma Export (C, u00173, "system__img_lluB");
   u00174 : constant Version_32 := 16#3b7a9044#;
   pragma Export (C, u00174, "system__img_lluS");
   u00175 : constant Version_32 := 16#16458a73#;
   pragma Export (C, u00175, "system__powten_tableS");
   u00176 : constant Version_32 := 16#c2ca0511#;
   pragma Export (C, u00176, "system__val_realB");
   u00177 : constant Version_32 := 16#b81c9b15#;
   pragma Export (C, u00177, "system__val_realS");
   u00178 : constant Version_32 := 16#b2a569d2#;
   pragma Export (C, u00178, "system__exn_llfB");
   u00179 : constant Version_32 := 16#fa4b57d8#;
   pragma Export (C, u00179, "system__exn_llfS");
   u00180 : constant Version_32 := 16#1e40f010#;
   pragma Export (C, u00180, "system__fat_fltS");
   u00181 : constant Version_32 := 16#3c83100c#;
   pragma Export (C, u00181, "pseudo_code__tableB");
   u00182 : constant Version_32 := 16#bc880154#;
   pragma Export (C, u00182, "pseudo_code__tableS");
   u00183 : constant Version_32 := 16#273384e4#;
   pragma Export (C, u00183, "system__img_enum_newB");
   u00184 : constant Version_32 := 16#2779eac4#;
   pragma Export (C, u00184, "system__img_enum_newS");
   u00185 : constant Version_32 := 16#a5f79485#;
   pragma Export (C, u00185, "ma_syntaxB");
   u00186 : constant Version_32 := 16#6595ac31#;
   pragma Export (C, u00186, "ma_syntaxS");
   u00187 : constant Version_32 := 16#84a1c37b#;
   pragma Export (C, u00187, "ma_syntax_gotoS");
   u00188 : constant Version_32 := 16#cfd37fb8#;
   pragma Export (C, u00188, "ma_syntax_shift_reduceS");
   u00189 : constant Version_32 := 16#c134caab#;
   pragma Export (C, u00189, "ma_syntax_tokensS");
   u00190 : constant Version_32 := 16#2b70b149#;
   pragma Export (C, u00190, "system__concat_3B");
   u00191 : constant Version_32 := 16#4d45b0a1#;
   pragma Export (C, u00191, "system__concat_3S");
   u00192 : constant Version_32 := 16#932a4690#;
   pragma Export (C, u00192, "system__concat_4B");
   u00193 : constant Version_32 := 16#3851c724#;
   pragma Export (C, u00193, "system__concat_4S");
   u00194 : constant Version_32 := 16#608e2cd1#;
   pragma Export (C, u00194, "system__concat_5B");
   u00195 : constant Version_32 := 16#c16baf2a#;
   pragma Export (C, u00195, "system__concat_5S");
   u00196 : constant Version_32 := 16#6ea01b5b#;
   pragma Export (C, u00196, "ma_lexicoB");
   u00197 : constant Version_32 := 16#e3ab205d#;
   pragma Export (C, u00197, "ma_lexicoS");
   u00198 : constant Version_32 := 16#70258d56#;
   pragma Export (C, u00198, "ma_dictB");
   u00199 : constant Version_32 := 16#0f327c77#;
   pragma Export (C, u00199, "ma_dictS");
   u00200 : constant Version_32 := 16#e3126c05#;
   pragma Export (C, u00200, "ma_token_ioB");
   u00201 : constant Version_32 := 16#a8688331#;
   pragma Export (C, u00201, "ma_token_ioS");
   u00202 : constant Version_32 := 16#f08789ae#;
   pragma Export (C, u00202, "ada__text_io__enumeration_auxB");
   u00203 : constant Version_32 := 16#e281a621#;
   pragma Export (C, u00203, "ada__text_io__enumeration_auxS");
   u00204 : constant Version_32 := 16#bc471de0#;
   pragma Export (C, u00204, "system__val_enumB");
   u00205 : constant Version_32 := 16#fd2fae91#;
   pragma Export (C, u00205, "system__val_enumS");
   u00206 : constant Version_32 := 16#94f1c598#;
   pragma Export (C, u00206, "ma_lexico_dfaB");
   u00207 : constant Version_32 := 16#9f5374a1#;
   pragma Export (C, u00207, "ma_lexico_dfaS");
   u00208 : constant Version_32 := 16#2237598c#;
   pragma Export (C, u00208, "ma_lexico_ioB");
   u00209 : constant Version_32 := 16#08e369a0#;
   pragma Export (C, u00209, "ma_lexico_ioS");
   u00210 : constant Version_32 := 16#0070e500#;
   pragma Export (C, u00210, "lecture_entiersB");
   u00211 : constant Version_32 := 16#4fd77a89#;
   pragma Export (C, u00211, "lecture_entiersS");
   u00212 : constant Version_32 := 16#551526e8#;
   pragma Export (C, u00212, "optionsB");
   u00213 : constant Version_32 := 16#583280c6#;
   pragma Export (C, u00213, "optionsS");
   u00214 : constant Version_32 := 16#ec10f679#;
   pragma Export (C, u00214, "config_machineS");
   u00215 : constant Version_32 := 16#69a1ba88#;
   pragma Export (C, u00215, "partie_opB");
   u00216 : constant Version_32 := 16#18c17d20#;
   pragma Export (C, u00216, "partie_opS");
   u00217 : constant Version_32 := 16#adb6d201#;
   pragma Export (C, u00217, "ada__strings__fixedB");
   u00218 : constant Version_32 := 16#a86b22b3#;
   pragma Export (C, u00218, "ada__strings__fixedS");
   u00219 : constant Version_32 := 16#60da0992#;
   pragma Export (C, u00219, "ada__strings__searchB");
   u00220 : constant Version_32 := 16#c1ab8667#;
   pragma Export (C, u00220, "ada__strings__searchS");
   u00221 : constant Version_32 := 16#b82c9e23#;
   pragma Export (C, u00221, "arrondisB");
   u00222 : constant Version_32 := 16#746a376a#;
   pragma Export (C, u00222, "arrondisS");
   u00223 : constant Version_32 := 16#d86b564b#;
   pragma Export (C, u00223, "clocksB");
   u00224 : constant Version_32 := 16#22e3510f#;
   pragma Export (C, u00224, "clocksS");
   u00225 : constant Version_32 := 16#9a2e29aa#;
   pragma Export (C, u00225, "ada__calendarB");
   u00226 : constant Version_32 := 16#c4f07049#;
   pragma Export (C, u00226, "ada__calendarS");
   u00227 : constant Version_32 := 16#51f2d040#;
   pragma Export (C, u00227, "system__os_primitivesB");
   u00228 : constant Version_32 := 16#41c889f2#;
   pragma Export (C, u00228, "system__os_primitivesS");
   u00229 : constant Version_32 := 16#69f6ee6b#;
   pragma Export (C, u00229, "interfaces__c__stringsB");
   u00230 : constant Version_32 := 16#603c1c44#;
   pragma Export (C, u00230, "interfaces__c__stringsS");
   u00231 : constant Version_32 := 16#3a147135#;
   pragma Export (C, u00231, "lecture_flottantsB");
   u00232 : constant Version_32 := 16#1ac5c741#;
   pragma Export (C, u00232, "lecture_flottantsS");
   u00233 : constant Version_32 := 16#9526d2ce#;
   pragma Export (C, u00233, "partie_op__tempsB");
   u00234 : constant Version_32 := 16#696bed65#;
   pragma Export (C, u00234, "partie_op__tempsS");
   u00235 : constant Version_32 := 16#ed063051#;
   pragma Export (C, u00235, "system__fat_sfltS");
   u00236 : constant Version_32 := 16#c65e3fd5#;
   pragma Export (C, u00236, "utf8_esB");
   u00237 : constant Version_32 := 16#6b8a2e70#;
   pragma Export (C, u00237, "utf8_esS");

   --  BEGIN ELABORATION ORDER
   --  ada%s
   --  ada.characters%s
   --  ada.characters.latin_1%s
   --  interfaces%s
   --  system%s
   --  system.address_operations%s
   --  system.address_operations%b
   --  system.exn_llf%s
   --  system.exn_llf%b
   --  system.float_control%s
   --  system.float_control%b
   --  system.img_bool%s
   --  system.img_bool%b
   --  system.img_enum_new%s
   --  system.img_enum_new%b
   --  system.img_int%s
   --  system.img_int%b
   --  system.img_lli%s
   --  system.img_lli%b
   --  system.io%s
   --  system.io%b
   --  system.os_primitives%s
   --  system.os_primitives%b
   --  system.parameters%s
   --  system.parameters%b
   --  system.crtl%s
   --  interfaces.c_streams%s
   --  interfaces.c_streams%b
   --  system.powten_table%s
   --  system.storage_elements%s
   --  system.storage_elements%b
   --  system.stack_checking%s
   --  system.stack_checking%b
   --  system.string_hash%s
   --  system.string_hash%b
   --  system.htable%s
   --  system.htable%b
   --  system.strings%s
   --  system.strings%b
   --  system.traceback_entries%s
   --  system.traceback_entries%b
   --  system.unsigned_types%s
   --  system.img_biu%s
   --  system.img_biu%b
   --  system.img_llb%s
   --  system.img_llb%b
   --  system.img_llu%s
   --  system.img_llu%b
   --  system.img_llw%s
   --  system.img_llw%b
   --  system.img_uns%s
   --  system.img_uns%b
   --  system.img_wiu%s
   --  system.img_wiu%b
   --  system.wch_con%s
   --  system.wch_con%b
   --  system.wch_jis%s
   --  system.wch_jis%b
   --  system.wch_cnv%s
   --  system.wch_cnv%b
   --  system.compare_array_unsigned_8%s
   --  system.compare_array_unsigned_8%b
   --  system.concat_2%s
   --  system.concat_2%b
   --  system.concat_3%s
   --  system.concat_3%b
   --  system.concat_4%s
   --  system.concat_4%b
   --  system.concat_5%s
   --  system.concat_5%b
   --  system.traceback%s
   --  system.traceback%b
   --  system.case_util%s
   --  system.standard_library%s
   --  system.exception_traces%s
   --  ada.exceptions%s
   --  system.wch_stw%s
   --  system.val_util%s
   --  system.val_llu%s
   --  system.val_lli%s
   --  system.os_lib%s
   --  system.bit_ops%s
   --  ada.characters.handling%s
   --  ada.exceptions.traceback%s
   --  ada.exceptions.last_chance_handler%s
   --  system.secondary_stack%s
   --  system.case_util%b
   --  system.address_image%s
   --  system.bounded_strings%s
   --  system.exceptions_debug%s
   --  system.exceptions_debug%b
   --  system.wch_stw%b
   --  system.val_util%b
   --  system.val_llu%b
   --  system.val_lli%b
   --  system.bit_ops%b
   --  ada.exceptions.traceback%b
   --  system.soft_links%s
   --  system.exception_table%s
   --  system.exception_table%b
   --  ada.io_exceptions%s
   --  ada.strings%s
   --  ada.containers%s
   --  system.exceptions%s
   --  system.exceptions%b
   --  system.soft_links.initialize%s
   --  system.soft_links.initialize%b
   --  system.soft_links%b
   --  ada.exceptions.last_chance_handler%b
   --  system.secondary_stack%b
   --  system.address_image%b
   --  system.bounded_strings%b
   --  system.exception_traces%b
   --  system.memory%s
   --  system.memory%b
   --  system.os_lib%b
   --  ada.strings.maps%s
   --  ada.strings.maps.constants%s
   --  ada.characters.handling%b
   --  interfaces.c%s
   --  system.exceptions.machine%s
   --  system.exceptions.machine%b
   --  system.standard_library%b
   --  system.mmap%s
   --  ada.strings.maps%b
   --  interfaces.c%b
   --  system.object_reader%s
   --  system.dwarf_lines%s
   --  system.dwarf_lines%b
   --  system.mmap.unix%s
   --  system.mmap.os_interface%s
   --  system.mmap%b
   --  system.traceback.symbolic%s
   --  system.traceback.symbolic%b
   --  ada.exceptions%b
   --  system.object_reader%b
   --  system.mmap.os_interface%b
   --  ada.command_line%s
   --  ada.command_line%b
   --  ada.strings.search%s
   --  ada.strings.search%b
   --  ada.strings.fixed%s
   --  ada.strings.fixed%b
   --  ada.tags%s
   --  ada.tags%b
   --  ada.streams%s
   --  ada.streams%b
   --  interfaces.c.strings%s
   --  interfaces.c.strings%b
   --  system.fat_flt%s
   --  system.fat_llf%s
   --  system.fat_sflt%s
   --  system.file_control_block%s
   --  system.finalization_root%s
   --  system.finalization_root%b
   --  ada.finalization%s
   --  system.file_io%s
   --  system.file_io%b
   --  system.img_real%s
   --  system.img_real%b
   --  system.storage_pools%s
   --  system.storage_pools%b
   --  system.finalization_masters%s
   --  system.finalization_masters%b
   --  system.val_enum%s
   --  system.val_enum%b
   --  system.val_real%s
   --  system.val_real%b
   --  system.val_uns%s
   --  system.val_uns%b
   --  system.val_int%s
   --  system.val_int%b
   --  ada.calendar%s
   --  ada.calendar%b
   --  ada.text_io%s
   --  ada.text_io%b
   --  ada.text_io.generic_aux%s
   --  ada.text_io.generic_aux%b
   --  ada.text_io.enumeration_aux%s
   --  ada.text_io.enumeration_aux%b
   --  ada.text_io.float_aux%s
   --  ada.text_io.float_aux%b
   --  ada.text_io.integer_aux%s
   --  ada.text_io.integer_aux%b
   --  ada.integer_text_io%s
   --  ada.integer_text_io%b
   --  system.pool_global%s
   --  system.pool_global%b
   --  arrondis%s
   --  arrondis%b
   --  config_machine%s
   --  ma_lexico_dfa%s
   --  ma_lexico_dfa%b
   --  ma_lexico_io%s
   --  ma_lexico_io%b
   --  ma_syntax_goto%s
   --  types_base%s
   --  types_base%b
   --  clocks%s
   --  clocks%b
   --  entier_es%s
   --  entier_es%b
   --  flottant_es%s
   --  flottant_es%b
   --  lecture_entiers%s
   --  lecture_entiers%b
   --  lecture_flottants%s
   --  lecture_flottants%b
   --  mes_tables%s
   --  mes_tables%b
   --  options%s
   --  options%b
   --  pseudo_code%s
   --  pseudo_code.table%s
   --  pseudo_code.table%b
   --  pseudo_code%b
   --  ma_detiq%s
   --  ma_detiq%b
   --  ma_syntax_tokens%s
   --  ma_syntax_shift_reduce%s
   --  ma_token_io%s
   --  ma_token_io%b
   --  ma_dict%s
   --  ma_dict%b
   --  ma_lexico%s
   --  ma_lexico%b
   --  ma_syntax%s
   --  ma_syntax%b
   --  assembleur%s
   --  assembleur%b
   --  utf8_es%s
   --  utf8_es%b
   --  partie_op%s
   --  partie_op.temps%s
   --  partie_op.temps%b
   --  partie_op%b
   --  ima%b
   --  END ELABORATION ORDER

end ada_main;
