# Dev overrides
{{ $greeting_nw_mode := "bridge" }}
{{ $nginx_nw_mode := "bridge" }}
{{ $test_nw_mode := "bridge" }}
{{ $int_nw_mode := "bridge" }}
{{ $nfs_nw_mode := "bridge" }}
{{ $ftp_nw_mode := "bridge" }}

{{ $is_vagrant := false }}
{{ $is_dev := true }}

{{ $greeting_count := "1" }}
{{ $nginx_count := "1" }}
{{ $int_count := "0" }}
{{ $test_count := "1" }}
{{ $nfs_count := "1" }}
{{ $ftp_count := "0" }}
{{ $enable_code_coverage := true }}