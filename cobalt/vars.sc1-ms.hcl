{{ $greeting_count := "3" }}
# This is being done since in SC1, sampleapp epg is messed up and not usable anymore.
{{ $greeting_nw_mode := printf "sampleApp-app-epg/%s" $tenant }}
{{ $nginx_nw_mode := printf "sampleApp-web-epg/%s" $tenant }}
