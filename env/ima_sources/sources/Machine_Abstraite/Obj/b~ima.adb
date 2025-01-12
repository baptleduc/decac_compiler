pragma Warnings (Off);
pragma Ada_95;
pragma Source_File_Name (ada_main, Spec_File_Name => "b~ima.ads");
pragma Source_File_Name (ada_main, Body_File_Name => "b~ima.adb");
pragma Suppress (Overflow_Check);
with Ada.Exceptions;

package body ada_main is

   E075 : Short_Integer; pragma Import (Ada, E075, "system__os_lib_E");
   E015 : Short_Integer; pragma Import (Ada, E015, "system__soft_links_E");
   E025 : Short_Integer; pragma Import (Ada, E025, "system__exception_table_E");
   E070 : Short_Integer; pragma Import (Ada, E070, "ada__io_exceptions_E");
   E055 : Short_Integer; pragma Import (Ada, E055, "ada__strings_E");
   E040 : Short_Integer; pragma Import (Ada, E040, "ada__containers_E");
   E027 : Short_Integer; pragma Import (Ada, E027, "system__exceptions_E");
   E017 : Short_Integer; pragma Import (Ada, E017, "system__soft_links__initialize_E");
   E057 : Short_Integer; pragma Import (Ada, E057, "ada__strings__maps_E");
   E061 : Short_Integer; pragma Import (Ada, E061, "ada__strings__maps__constants_E");
   E045 : Short_Integer; pragma Import (Ada, E045, "interfaces__c_E");
   E081 : Short_Integer; pragma Import (Ada, E081, "system__object_reader_E");
   E050 : Short_Integer; pragma Import (Ada, E050, "system__dwarf_lines_E");
   E039 : Short_Integer; pragma Import (Ada, E039, "system__traceback__symbolic_E");
   E105 : Short_Integer; pragma Import (Ada, E105, "ada__tags_E");
   E103 : Short_Integer; pragma Import (Ada, E103, "ada__streams_E");
   E230 : Short_Integer; pragma Import (Ada, E230, "interfaces__c__strings_E");
   E117 : Short_Integer; pragma Import (Ada, E117, "system__file_control_block_E");
   E116 : Short_Integer; pragma Import (Ada, E116, "system__finalization_root_E");
   E114 : Short_Integer; pragma Import (Ada, E114, "ada__finalization_E");
   E113 : Short_Integer; pragma Import (Ada, E113, "system__file_io_E");
   E155 : Short_Integer; pragma Import (Ada, E155, "system__storage_pools_E");
   E151 : Short_Integer; pragma Import (Ada, E151, "system__finalization_masters_E");
   E226 : Short_Integer; pragma Import (Ada, E226, "ada__calendar_E");
   E101 : Short_Integer; pragma Import (Ada, E101, "ada__text_io_E");
   E157 : Short_Integer; pragma Import (Ada, E157, "system__pool_global_E");
   E222 : Short_Integer; pragma Import (Ada, E222, "arrondis_E");
   E207 : Short_Integer; pragma Import (Ada, E207, "ma_lexico_dfa_E");
   E209 : Short_Integer; pragma Import (Ada, E209, "ma_lexico_io_E");
   E143 : Short_Integer; pragma Import (Ada, E143, "types_base_E");
   E224 : Short_Integer; pragma Import (Ada, E224, "clocks_E");
   E211 : Short_Integer; pragma Import (Ada, E211, "lecture_entiers_E");
   E232 : Short_Integer; pragma Import (Ada, E232, "lecture_flottants_E");
   E141 : Short_Integer; pragma Import (Ada, E141, "mes_tables_E");
   E213 : Short_Integer; pragma Import (Ada, E213, "options_E");
   E161 : Short_Integer; pragma Import (Ada, E161, "pseudo_code_E");
   E182 : Short_Integer; pragma Import (Ada, E182, "pseudo_code__table_E");
   E139 : Short_Integer; pragma Import (Ada, E139, "ma_detiq_E");
   E189 : Short_Integer; pragma Import (Ada, E189, "ma_syntax_tokens_E");
   E188 : Short_Integer; pragma Import (Ada, E188, "ma_syntax_shift_reduce_E");
   E199 : Short_Integer; pragma Import (Ada, E199, "ma_dict_E");
   E197 : Short_Integer; pragma Import (Ada, E197, "ma_lexico_E");
   E186 : Short_Integer; pragma Import (Ada, E186, "ma_syntax_E");
   E137 : Short_Integer; pragma Import (Ada, E137, "assembleur_E");
   E237 : Short_Integer; pragma Import (Ada, E237, "utf8_es_E");
   E216 : Short_Integer; pragma Import (Ada, E216, "partie_op_E");
   E234 : Short_Integer; pragma Import (Ada, E234, "partie_op__temps_E");

   Sec_Default_Sized_Stacks : array (1 .. 1) of aliased System.Secondary_Stack.SS_Stack (System.Parameters.Runtime_Default_Sec_Stack_Size);

   Local_Priority_Specific_Dispatching : constant String := "";
   Local_Interrupt_States : constant String := "";

   Is_Elaborated : Boolean := False;

   procedure finalize_library is
   begin
      E157 := E157 - 1;
      declare
         procedure F1;
         pragma Import (Ada, F1, "system__pool_global__finalize_spec");
      begin
         F1;
      end;
      E101 := E101 - 1;
      declare
         procedure F2;
         pragma Import (Ada, F2, "ada__text_io__finalize_spec");
      begin
         F2;
      end;
      E151 := E151 - 1;
      declare
         procedure F3;
         pragma Import (Ada, F3, "system__finalization_masters__finalize_spec");
      begin
         F3;
      end;
      declare
         procedure F4;
         pragma Import (Ada, F4, "system__file_io__finalize_body");
      begin
         E113 := E113 - 1;
         F4;
      end;
      declare
         procedure Reraise_Library_Exception_If_Any;
            pragma Import (Ada, Reraise_Library_Exception_If_Any, "__gnat_reraise_library_exception_if_any");
      begin
         Reraise_Library_Exception_If_Any;
      end;
   end finalize_library;

   procedure adafinal is
      procedure s_stalib_adafinal;
      pragma Import (C, s_stalib_adafinal, "system__standard_library__adafinal");

      procedure Runtime_Finalize;
      pragma Import (C, Runtime_Finalize, "__gnat_runtime_finalize");

   begin
      if not Is_Elaborated then
         return;
      end if;
      Is_Elaborated := False;
      Runtime_Finalize;
      s_stalib_adafinal;
   end adafinal;

   type No_Param_Proc is access procedure;

   procedure adainit is
      Main_Priority : Integer;
      pragma Import (C, Main_Priority, "__gl_main_priority");
      Time_Slice_Value : Integer;
      pragma Import (C, Time_Slice_Value, "__gl_time_slice_val");
      WC_Encoding : Character;
      pragma Import (C, WC_Encoding, "__gl_wc_encoding");
      Locking_Policy : Character;
      pragma Import (C, Locking_Policy, "__gl_locking_policy");
      Queuing_Policy : Character;
      pragma Import (C, Queuing_Policy, "__gl_queuing_policy");
      Task_Dispatching_Policy : Character;
      pragma Import (C, Task_Dispatching_Policy, "__gl_task_dispatching_policy");
      Priority_Specific_Dispatching : System.Address;
      pragma Import (C, Priority_Specific_Dispatching, "__gl_priority_specific_dispatching");
      Num_Specific_Dispatching : Integer;
      pragma Import (C, Num_Specific_Dispatching, "__gl_num_specific_dispatching");
      Main_CPU : Integer;
      pragma Import (C, Main_CPU, "__gl_main_cpu");
      Interrupt_States : System.Address;
      pragma Import (C, Interrupt_States, "__gl_interrupt_states");
      Num_Interrupt_States : Integer;
      pragma Import (C, Num_Interrupt_States, "__gl_num_interrupt_states");
      Unreserve_All_Interrupts : Integer;
      pragma Import (C, Unreserve_All_Interrupts, "__gl_unreserve_all_interrupts");
      Exception_Tracebacks : Integer;
      pragma Import (C, Exception_Tracebacks, "__gl_exception_tracebacks");
      Detect_Blocking : Integer;
      pragma Import (C, Detect_Blocking, "__gl_detect_blocking");
      Default_Stack_Size : Integer;
      pragma Import (C, Default_Stack_Size, "__gl_default_stack_size");
      Default_Secondary_Stack_Size : System.Parameters.Size_Type;
      pragma Import (C, Default_Secondary_Stack_Size, "__gnat_default_ss_size");
      Leap_Seconds_Support : Integer;
      pragma Import (C, Leap_Seconds_Support, "__gl_leap_seconds_support");
      Bind_Env_Addr : System.Address;
      pragma Import (C, Bind_Env_Addr, "__gl_bind_env_addr");

      procedure Runtime_Initialize (Install_Handler : Integer);
      pragma Import (C, Runtime_Initialize, "__gnat_runtime_initialize");

      Finalize_Library_Objects : No_Param_Proc;
      pragma Import (C, Finalize_Library_Objects, "__gnat_finalize_library_objects");
      Binder_Sec_Stacks_Count : Natural;
      pragma Import (Ada, Binder_Sec_Stacks_Count, "__gnat_binder_ss_count");
      Default_Sized_SS_Pool : System.Address;
      pragma Import (Ada, Default_Sized_SS_Pool, "__gnat_default_ss_pool");

   begin
      if Is_Elaborated then
         return;
      end if;
      Is_Elaborated := True;
      Main_Priority := -1;
      Time_Slice_Value := -1;
      WC_Encoding := 'b';
      Locking_Policy := ' ';
      Queuing_Policy := ' ';
      Task_Dispatching_Policy := ' ';
      Priority_Specific_Dispatching :=
        Local_Priority_Specific_Dispatching'Address;
      Num_Specific_Dispatching := 0;
      Main_CPU := -1;
      Interrupt_States := Local_Interrupt_States'Address;
      Num_Interrupt_States := 0;
      Unreserve_All_Interrupts := 0;
      Exception_Tracebacks := 1;
      Detect_Blocking := 0;
      Default_Stack_Size := -1;
      Leap_Seconds_Support := 0;

      ada_main'Elab_Body;
      Default_Secondary_Stack_Size := System.Parameters.Runtime_Default_Sec_Stack_Size;
      Binder_Sec_Stacks_Count := 1;
      Default_Sized_SS_Pool := Sec_Default_Sized_Stacks'Address;

      Runtime_Initialize (1);

      Finalize_Library_Objects := finalize_library'access;

      System.Soft_Links'Elab_Spec;
      System.Exception_Table'Elab_Body;
      E025 := E025 + 1;
      Ada.Io_Exceptions'Elab_Spec;
      E070 := E070 + 1;
      Ada.Strings'Elab_Spec;
      E055 := E055 + 1;
      Ada.Containers'Elab_Spec;
      E040 := E040 + 1;
      System.Exceptions'Elab_Spec;
      E027 := E027 + 1;
      System.Soft_Links.Initialize'Elab_Body;
      E017 := E017 + 1;
      E015 := E015 + 1;
      System.Os_Lib'Elab_Body;
      E075 := E075 + 1;
      Ada.Strings.Maps'Elab_Spec;
      Ada.Strings.Maps.Constants'Elab_Spec;
      E061 := E061 + 1;
      Interfaces.C'Elab_Spec;
      E057 := E057 + 1;
      E045 := E045 + 1;
      System.Object_Reader'Elab_Spec;
      System.Dwarf_Lines'Elab_Spec;
      E050 := E050 + 1;
      System.Traceback.Symbolic'Elab_Body;
      E039 := E039 + 1;
      E081 := E081 + 1;
      Ada.Tags'Elab_Spec;
      Ada.Tags'Elab_Body;
      E105 := E105 + 1;
      Ada.Streams'Elab_Spec;
      E103 := E103 + 1;
      Interfaces.C.Strings'Elab_Spec;
      E230 := E230 + 1;
      System.File_Control_Block'Elab_Spec;
      E117 := E117 + 1;
      System.Finalization_Root'Elab_Spec;
      E116 := E116 + 1;
      Ada.Finalization'Elab_Spec;
      E114 := E114 + 1;
      System.File_Io'Elab_Body;
      E113 := E113 + 1;
      System.Storage_Pools'Elab_Spec;
      E155 := E155 + 1;
      System.Finalization_Masters'Elab_Spec;
      System.Finalization_Masters'Elab_Body;
      E151 := E151 + 1;
      Ada.Calendar'Elab_Spec;
      Ada.Calendar'Elab_Body;
      E226 := E226 + 1;
      Ada.Text_Io'Elab_Spec;
      Ada.Text_Io'Elab_Body;
      E101 := E101 + 1;
      System.Pool_Global'Elab_Spec;
      E157 := E157 + 1;
      Arrondis'Elab_Spec;
      E222 := E222 + 1;
      E207 := E207 + 1;
      MA_LEXICO_IO'ELAB_SPEC;
      E209 := E209 + 1;
      E143 := E143 + 1;
      Clocks'Elab_Body;
      E224 := E224 + 1;
      E211 := E211 + 1;
      E232 := E232 + 1;
      E141 := E141 + 1;
      Options'Elab_Body;
      E213 := E213 + 1;
      Pseudo_Code'Elab_Spec;
      Pseudo_Code.Table'Elab_Spec;
      Pseudo_Code.Table'Elab_Body;
      E182 := E182 + 1;
      Pseudo_Code'Elab_Body;
      E161 := E161 + 1;
      ma_detiq'elab_spec;
      MA_DETIQ'ELAB_BODY;
      E139 := E139 + 1;
      Ma_Syntax_Tokens'Elab_Spec;
      E189 := E189 + 1;
      Ma_Syntax_Shift_Reduce'Elab_Spec;
      E188 := E188 + 1;
      Ma_Dict'Elab_Body;
      E199 := E199 + 1;
      ma_lexico'elab_spec;
      MA_LEXICO'ELAB_BODY;
      E197 := E197 + 1;
      E186 := E186 + 1;
      Assembleur'Elab_Spec;
      E137 := E137 + 1;
      E237 := E237 + 1;
      Partie_Op'Elab_Spec;
      E234 := E234 + 1;
      Partie_Op'Elab_Body;
      E216 := E216 + 1;
   end adainit;

   procedure Ada_Main_Program;
   pragma Import (Ada, Ada_Main_Program, "_ada_ima");

   function main
     (argc : Integer;
      argv : System.Address;
      envp : System.Address)
      return Integer
   is
      procedure Initialize (Addr : System.Address);
      pragma Import (C, Initialize, "__gnat_initialize");

      procedure Finalize;
      pragma Import (C, Finalize, "__gnat_finalize");
      SEH : aliased array (1 .. 2) of Integer;

      Ensure_Reference : aliased System.Address := Ada_Main_Program_Name'Address;
      pragma Volatile (Ensure_Reference);

   begin
      gnat_argc := argc;
      gnat_argv := argv;
      gnat_envp := envp;

      Initialize (SEH'Address);
      adainit;
      Ada_Main_Program;
      adafinal;
      Finalize;
      return (gnat_exit_status);
   end;

--  BEGIN Object file/option list
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/arrondis.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/config_machine.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/ma_lexico_dfa.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/ma_lexico_io.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/ma_syntax_goto.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/types_base.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/clocks.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/entier_es.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/flottant_es.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/lecture_entiers.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/lecture_flottants.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/mes_tables.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/options.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/pseudo_code-table.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/pseudo_code.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/ma_detiq.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/ma_syntax_tokens.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/ma_syntax_shift_reduce.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/ma_token_io.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/ma_dict.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/ma_lexico.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/ma_syntax.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/assembleur.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/utf8_es.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/partie_op-temps.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/partie_op.o
   --   /home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/ima.o
   --   -L/home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/
   --   -L/home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/Obj/
   --   -L/home/gl/Compil/Projet_GL/Etudiants/global/sources/Machine_Abstraite/L_Interp/Src/../..//Commun/Obj/
   --   -L/usr/lib/gcc/x86_64-linux-gnu/9/adalib/
   --   -static
   --   -lgnat
   --   -ldl
--  END Object file/option list   

end ada_main;
