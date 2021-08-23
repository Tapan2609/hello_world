# Production overrides
{{ $greeting_nw_mode := printf "sampleApp-app-epg/%s" $tenant }}
{{ $nginx_nw_mode := printf "sampleApp-web-epg/%s" $tenant }}
{{ $test_nw_mode := printf "sampleApp-app-epg/%s" $tenant }}
{{ $int_nw_mode := printf "sampleApp-app-epg/%s" $tenant }}
{{ $nfs_nw_mode := printf "sampleApp-app-epg/%s" $tenant }}
{{ $ftp_nw_mode := printf "sampleApp-app-epg/%s" $tenant }}

{{ $greeting_count := "6" }}
{{ $nginx_count := "2" }}
{{ $int_count := "0" }}
{{ $test_count := "0" }}
{{ $nfs_count := "0" }}
{{ $ftp_count := "0" }}

{{ $greeting_cpu := "1200" }}
{{ $greeting_memory := "4096" }}

{{ $is_vagrant := false }}
{{ $is_dev := false }}
{{ $enable_code_coverage := false }}